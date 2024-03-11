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
import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@Service
public class CarrinhoService {

	@Autowired
	private TokenJWTService tokenJWTService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private FileStorageService fileService;
	
	@Transactional
	public void adicionarProdutosNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new RuntimeException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ itemCarrinhoDTO.quantidade());
		}
		
		var idCliente = tokenJWTService.getClaimIdJWT();
		
		var cliente = clienteRepository.getReferenceById(idCliente);

		var produto = produtoRepository.getReferenceById(itemCarrinhoDTO.idProduto());

		var optionalCarrinho = carrinhoRepository.findByCliente(cliente);
		
		if(optionalCarrinho.isPresent()) {
			Carrinho carrinho = optionalCarrinho.get();

			List<ItemCarrinho> itensCarrinho = carrinho.getItemsCarrinho();
	
			for (ItemCarrinho itemCarrinho : itensCarrinho) {
				if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.idProduto()) {
					itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + itemCarrinhoDTO.quantidade());
					carrinhoRepository.save(carrinho);
					return;
				}
			}
			carrinho.getItemsCarrinho().add(new ItemCarrinho(produto.getId(), itemCarrinhoDTO.quantidade()));
	
			carrinhoRepository.save(carrinho);
		}
		else {
			Carrinho carrinho = new Carrinho(cliente, produto.getId(), itemCarrinhoDTO.quantidade());
			carrinhoRepository.save(carrinho);
		}
	}

	public MostrarCarrinhoClienteDTO pegarCarrinhoCliente() {
		var idCliente = tokenJWTService.getClaimIdJWT();

		var cliente = clienteRepository.getReferenceById(idCliente);
		
		Carrinho carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		List<Long> ids = carrinho.getItemsCarrinho().stream()
				.map(ItemCarrinho::getProdutoId)
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
	public void setarQuantidadeProdutoNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (!produtoRepository.existsById(itemCarrinhoDTO.idProduto())) {
		    throw new RuntimeException("Produto não encontrado para o ID: " + itemCarrinhoDTO.idProduto());
		}
	
		var idCliente = tokenJWTService.getClaimIdJWT();
		
		var cliente = clienteRepository.getReferenceById(idCliente);
			
		var carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.idProduto()) {
				itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}

	@Transactional
	public void deletarProdutoNoCarrinho(Long idProduto) 
		{
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		var carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProdutoId() == idProduto) {
				iterator.remove();
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}
	
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
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		Carrinho carrinho = verificarCarrinhoCliente(cliente);

		List<MostrarItemCarrinhoDTO> listMostrarItemCarrinho = carrinho.getItemsCarrinho().stream()
				.map(MostrarItemCarrinhoDTO::converterItemCarrinhoEmMostrarItemCarrinhoDTO)
				.collect(Collectors.toList());
		
		return listMostrarItemCarrinho;
//		List<MostrarItemCarrinhoDTO> produtosDesordenados = new ArrayList<>();
//		listMostrarItemCarrinho.forEach(id -> {
//			produtoService.verificarSeProdutoExistePorIdEAtivoTrue(id.idProduto());
//			MostrarItemCarrinhoDTO mostrarItemCarrinhoDTO = new MostrarItemCarrinhoDTO(id.idProduto(), id.quantidade());
//			produtosDesordenados.add(mostrarItemCarrinhoDTO);
//		});
//			
//		return listMostrarItemCarrinho.stream().map(id -> produtosDesordenados.stream()
//		                   .filter(produto -> produto.getId().equals(id.idProduto()))
//		                   .findFirst()
//		                   .orElse(null))
//						   .collect(Collectors.toList());
	}

}
