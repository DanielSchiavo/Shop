package br.com.danielschiavo.shop.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.DeletarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.carrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
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
	private CarrinhoRepository carrinhoRepository;
	
	public void adicionarProdutosNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new RuntimeException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: " + itemCarrinhoDTO.quantidade());
		}
		
		Produto produto = produtoService.getReferenceById(itemCarrinhoDTO.produto_id());
		
		Optional<Carrinho> optionalCarrinho = carrinhoRepository.findByClienteId(tokenJWTService.getClaimIdJWT());
		
		if (optionalCarrinho.isPresent()) {
			Carrinho carrinho = optionalCarrinho.get();
			
			List<ItemCarrinho> itensCarrinho = carrinho.getItensCarrinho();
			
			for (ItemCarrinho itemCarrinho : itensCarrinho) {
				if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.produto_id()) {
					itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + itemCarrinhoDTO.quantidade());
					carrinhoRepository.save(carrinho);
					return;
				}
			}
			carrinho.getItensCarrinho().add(new ItemCarrinho(itemCarrinhoDTO.produto_id(), itemCarrinhoDTO.quantidade()));
			
			carrinhoRepository.save(carrinho);
		}
		else {
		Carrinho carrinho = new Carrinho(tokenJWTService.getClaimIdJWT(), produto.getId(), itemCarrinhoDTO.quantidade());
		carrinhoRepository.save(carrinho);
		}
		}

	public MostrarCarrinhoClienteDTO pegarCarrinhoCliente() {
		Long clienteId = tokenJWTService.getClaimIdJWT();
		Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId).orElseThrow();
		
		List<Long> ids = carrinho.getItensCarrinho().stream().map(ItemCarrinho::getProdutoId)
        .collect(Collectors.toList());
		
		List<Produto> produtos = produtoRepository.findAllById(ids);
		
		BigDecimal valorTotal = BigDecimal.ZERO;
		List<MostrarItemCarrinhoClienteDTO> listaMostrarItensCarrinhoCliente = new ArrayList<MostrarItemCarrinhoClienteDTO>();
		
	    for (int i = 0; i < produtos.size(); i++) {
	    	
	    	listaMostrarItensCarrinhoCliente.add(new MostrarItemCarrinhoClienteDTO(produtos.get(i),
	    			produtoService.pegarPrimeiraImagemProduto(produtos.get(i).getArquivosProduto()),
	    			carrinho.getItensCarrinho().get(i).getQuantidade()));
	    	
	        valorTotal = valorTotal.add(new BigDecimal(carrinho.getItensCarrinho().get(i).getQuantidade())
	        											.multiply(produtos.get(i).getPreco()));
	    }
	    
	    return new MostrarCarrinhoClienteDTO(listaMostrarItensCarrinhoCliente, valorTotal);
	    
	}
	
	public void alterarQuantidadeProdutoNoCarrinho(ItemCarrinhoDTO itemCarrinhoDTO) {
		produtoService.getReferenceById(itemCarrinhoDTO.produto_id());
		Carrinho carrinho = carrinhoRepository.findByClienteId(tokenJWTService.getClaimIdJWT()).orElseThrow();
		
	    Iterator<ItemCarrinho> iterator = carrinho.getItensCarrinho().iterator();
	    while (iterator.hasNext()) {
	        ItemCarrinho itemCarrinho = iterator.next();
	        if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.produto_id()) {
	        	itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
	            carrinhoRepository.save(carrinho);
	            return;
	        }
	    }
	}

	public void deletarProdutoNoCarrinho(@Valid DeletarItemCarrinhoDTO itemCarrinhoDTO) {
		produtoService.getReferenceById(itemCarrinhoDTO.produtoId());
		Carrinho carrinho = carrinhoRepository.findByClienteId(tokenJWTService.getClaimIdJWT()).orElseThrow();
		
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
