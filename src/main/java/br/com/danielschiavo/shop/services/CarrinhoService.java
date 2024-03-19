package br.com.danielschiavo.shop.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@Service
public class CarrinhoService {

	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private FileStorageService fileService;
	
	@Transactional
	public void deletarProdutoNoCarrinhoPorIdToken(Long idProduto) 
		{
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Carrinho carrinho = cliente.getCarrinho();
		
		if (carrinho.getId() == null) {
			throw new ValidacaoException("Não existe um carrinho para o cliente de id número " + cliente.getId());
		}
		
		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProduto().getId() == idProduto) {
				iterator.remove();
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}
	
	public MostrarCarrinhoClienteDTO pegarCarrinhoClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Carrinho carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		List<Long> ids = carrinho.getItemsCarrinho().stream()
				.map(ic -> ic.getProduto().getId())
				.collect(Collectors.toList());

		List<Produto> produtosDesordenados = produtoRepository.findAllById(ids);
		
		List<Produto> produtosOrdenados = ids.stream()
		        .map(id -> produtosDesordenados.stream()
		                .filter(produto -> produto.getId().equals(id))
		                .findFirst()
		                .orElse(null))
		        .collect(Collectors.toList());

		BigDecimal valorTotal = BigDecimal.ZERO;
		List<MostrarItemCarrinhoClienteDTO> listaMostrarItensCarrinhoCliente = new ArrayList<MostrarItemCarrinhoClienteDTO>();

		for (int i = 0; i < produtosOrdenados.size(); i++) {
			String nomePrimeiraImagem = produtosOrdenados.get(i).pegarNomePrimeiraImagem();
			fileService.pegarArquivoProdutoPorNome(nomePrimeiraImagem);
			var mostrarItemCarrinhoClienteDTO = new MostrarItemCarrinhoClienteDTO(
												produtosOrdenados.get(i),
												fileService.pegarArquivoProdutoPorNome(nomePrimeiraImagem).bytesArquivo(),
												carrinho.getItemsCarrinho().get(i).getQuantidade());
			
			listaMostrarItensCarrinhoCliente.add(mostrarItemCarrinhoClienteDTO);

			valorTotal = valorTotal.add(new BigDecimal(carrinho.getItemsCarrinho().get(i).getQuantidade())
					.multiply(produtosOrdenados.get(i).getPreco()));
		}

		return new MostrarCarrinhoClienteDTO(carrinho.getId(), listaMostrarItensCarrinhoCliente, valorTotal);

	}
	
	@Transactional
	public void adicionarProdutosNoCarrinhoPorIdToken(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new RuntimeException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ itemCarrinhoDTO.quantidade());
		}
		Cliente cliente = usuarioAutenticadoService.getCliente();

		var produto = produtoRepository.getReferenceById(itemCarrinhoDTO.idProduto());

		var optionalCarrinho = carrinhoRepository.findByCliente(cliente);
		
		if(optionalCarrinho.isPresent()) {
			Carrinho carrinho = optionalCarrinho.get();

			List<ItemCarrinho> itensCarrinho = carrinho.getItemsCarrinho();
	
			for (ItemCarrinho itemCarrinho : itensCarrinho) {
				if (itemCarrinho.getProduto().getId() == itemCarrinhoDTO.idProduto()) {
					itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + itemCarrinhoDTO.quantidade());
					carrinhoRepository.save(carrinho);
					return;
				}
			}
			carrinho.getItemsCarrinho().add(new ItemCarrinho(null, itemCarrinhoDTO.quantidade(), produto, carrinho));
	
			carrinhoRepository.save(carrinho);
		}
		else {
			Carrinho carrinho = new Carrinho(cliente, produto, itemCarrinhoDTO.quantidade());
			carrinhoRepository.save(carrinho);
		}
	}

	@Transactional
	public void setarQuantidadeProdutoNoCarrinhoPorIdToken(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (!produtoRepository.existsById(itemCarrinhoDTO.idProduto())) {
		    throw new RuntimeException("Produto não encontrado para o ID: " + itemCarrinhoDTO.idProduto());
		}
	
		Cliente cliente = usuarioAutenticadoService.getCliente();
		var carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProduto().getId() == itemCarrinhoDTO.idProduto()) {
				if (itemCarrinhoDTO.quantidade() == 0) {
					iterator.remove();
					carrinhoRepository.save(carrinho);
					return;
				}
				itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public Carrinho verificarCarrinhoCliente(Cliente cliente) {
		Optional<Carrinho> optionalCliente = carrinhoRepository.findByCliente(cliente);
		if (optionalCliente.isPresent()) {
			return optionalCliente.get();
		}
		else {
			throw new ValidacaoException("Não existe um carrinho para o cliente de ID número " + cliente.getId());
		}
	}
	
	public List<MostrarItemCarrinhoDTO> pegarItensNoCarrinhoCliente() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Carrinho carrinho = verificarCarrinhoCliente(cliente);

		List<MostrarItemCarrinhoDTO> listMostrarItemCarrinho = carrinho.getItemsCarrinho().stream()
				.map(MostrarItemCarrinhoDTO::converterItemCarrinhoEmMostrarItemCarrinhoDTO)
				.collect(Collectors.toList());
		
		return listMostrarItemCarrinho;
	}

}
