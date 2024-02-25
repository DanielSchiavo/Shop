package br.com.danielschiavo.shop.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.DeletarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;
import jakarta.validation.Valid;

@Service
public class CarrinhoService {

	@Autowired
	private TokenJWTService tokenJWTService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	private Long idCliente = tokenJWTService.getClaimIdJWT();
	
	private Cliente cliente = clienteRepository.getReferenceById(idCliente);

	public void adicionarProdutosNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new RuntimeException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ itemCarrinhoDTO.quantidade());
		}

		var produto = produtoRepository.getReferenceById(itemCarrinhoDTO.idProduto());

		var optionalCarrinho = carrinhoRepository.findByCliente(cliente);
		
		if(optionalCarrinho.isPresent()) {
			Carrinho carrinho = optionalCarrinho.get();

			List<ItemCarrinho> itensCarrinho = carrinho.getItensCarrinho();
	
			for (ItemCarrinho itemCarrinho : itensCarrinho) {
				if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.idProduto()) {
					itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + itemCarrinhoDTO.quantidade());
					carrinhoRepository.save(carrinho);
					return;
				}
			}
			carrinho.getItensCarrinho().add(new ItemCarrinho(null, produto.getId(), itemCarrinhoDTO.quantidade(), carrinho));
	
			carrinhoRepository.save(carrinho);
		}
		else {
			Carrinho carrinho = new Carrinho(cliente, produto.getId(), itemCarrinhoDTO.quantidade());
			carrinhoRepository.save(carrinho);
		}
	}

	public MostrarCarrinhoClienteDTO pegarCarrinhoCliente() {
		
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		Carrinho carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		List<Long> ids = carrinho.getItensCarrinho().stream()
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

			var mostrarItemCarrinhoClienteDTO = new MostrarItemCarrinhoClienteDTO(
												produtosOrdenados.get(i),
												produtoService.pegarPrimeiraImagemProduto(produtosOrdenados.get(i).getArquivosProduto()),
												carrinho.getItensCarrinho().get(i).getQuantidade());
			
			listaMostrarItensCarrinhoCliente.add(mostrarItemCarrinhoClienteDTO);

			valorTotal = valorTotal.add(new BigDecimal(carrinho.getItensCarrinho().get(i).getQuantidade())
					.multiply(produtosOrdenados.get(i).getPreco()));
		}

		return new MostrarCarrinhoClienteDTO(listaMostrarItensCarrinhoCliente, valorTotal);

	}

	public void alterarQuantidadeProdutoNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (!produtoRepository.existsById(itemCarrinhoDTO.idProduto())) {
		    throw new RuntimeException("Produto não encontrado para o ID: " + itemCarrinhoDTO.idProduto());
		}
		
		var carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		Iterator<ItemCarrinho> iterator = carrinho.getItensCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.idProduto()) {
				itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}

	public void deletarProdutoNoCarrinho(@Valid DeletarItemCarrinhoDTO itemCarrinhoDTO) {
		var carrinho = carrinhoRepository.findByCliente(cliente).orElseThrow();

		Iterator<ItemCarrinho> iterator = carrinho.getItensCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.produtoId()) {
				iterator.remove();
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}

}
