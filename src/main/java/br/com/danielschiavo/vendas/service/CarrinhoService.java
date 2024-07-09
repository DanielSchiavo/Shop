package br.com.danielschiavo.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.service.produto.ProdutoService;
import br.com.danielschiavo.vendas.model.entity.Carrinho;
import br.com.danielschiavo.vendas.dto.request.AdicionarItemCarrinhoRequest;
import br.com.danielschiavo.vendas.model.entity.ItemCarrinho;
import br.com.danielschiavo.vendas.repository.CarrinhoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

@Service
@Setter
public class CarrinhoService {

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
    @Autowired
    private ProdutoService produtoService;

	@Transactional
	public void removerProdutoDoCarrinho(Long clienteId, Long... produtosId) {
		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);

		List<ItemCarrinho> itemsCarrinho = carrinho.getItemsCarrinho();
		itemsCarrinho.stream().filter(item -> Arrays.stream(produtosId).anyMatch(id -> item.getProdutoId().equals(id)))
				.forEach(carrinho::removerItemCarrinho);

		carrinhoRepository.save(carrinho);
	}
	
	public Carrinho pegarCarrinhoPorClienteId(Long clienteId) {
		return carrinhoRepository.findByClienteId(clienteId).orElseThrow(() -> new ValidacaoException("Usuario não possui carrinho"));
	}
	
	@Transactional
	public Carrinho adicionarProdutosNoCarrinhoPorIdToken(AdicionarItemCarrinhoRequest request, Long clienteId) {
		if (request.quantidade() <= 0) {
			throw new ValidacaoException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ request.quantidade());
		}

		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);
		carrinho.setDataEHoraAtualizacao(LocalDateTime.now());

		List<ItemCarrinho> itemsCarrinho = carrinho.getItemsCarrinho();
		Optional<ItemCarrinho> optionalItemCarrinho = itemsCarrinho.stream().filter(item -> item.getId().equals(request.produtoId())).findFirst();

		if (optionalItemCarrinho.isPresent()) {
			var itemCarrinho = optionalItemCarrinho.get();
			itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + request.quantidade());
		} else {
			Produto produto = produtoService.pegarProdutoPorId(request.produtoId());
			BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(request.quantidade()));
			ItemCarrinho itemCarrinho = ItemCarrinho.builder()
					.id(null)
					.quantidade(request.quantidade())
					.produtoId(request.produtoId())
					.subTotal(subTotal)
					.dataEHoraInsercao(LocalDateTime.now())
					.carrinho(carrinho).build();

			//No metodo adicionarItemCarrinho já faz a definição do novo valor total na entidade Carrinho
			carrinho.adicionarItemCarrinho(itemCarrinho);
		}

	    return carrinhoRepository.save(carrinho);
	}

	@Transactional
	public void setarQuantidadeProdutoNoCarrinho(AdicionarItemCarrinhoRequest request, Long clienteId) {
		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);

		ItemCarrinho itemCarrinho = carrinho.getItemsCarrinho().stream().filter(item -> item.getProdutoId().equals(request.produtoId()))
				.findFirst().orElseThrow(() -> new ValidacaoException("Esse produto não foi adicionado ao carrinho ainda"));

		if (request.quantidade() <= 0) {
			carrinho.removerItemCarrinho(itemCarrinho);
		} else {
			itemCarrinho.setQuantidade(request.quantidade());
		}

		carrinhoRepository.save(carrinho);
	}
}
