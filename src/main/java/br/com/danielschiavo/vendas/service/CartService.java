package br.com.danielschiavo.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.produto.model.entity.Product;
import br.com.danielschiavo.produto.service.product.ProductService;
import br.com.danielschiavo.vendas.model.entity.Carrinho;
import br.com.danielschiavo.vendas.model.entity.ItemCarrinho;
import br.com.danielschiavo.vendas.repository.CarrinhoRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

@Service
@Setter
public class CartService {

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
    @Autowired
    private ProductService produtoService;

	@Transactional
	public void removerProdutoDoCarrinho(Long clienteId, Long... produtosId) {
		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);

		List<ItemCarrinho> itemsCarrinho = carrinho.getItemsCarrinho();
		itemsCarrinho.stream().filter(item -> Arrays.stream(produtosId).anyMatch(id -> item.getProdutoId().equals(id)))
				.forEach(carrinho::removerItemCarrinho);

		carrinhoRepository.save(carrinho);
	}
	
	public Carrinho pegarCarrinhoPorClienteId(Long clienteId) {
		return carrinhoRepository.findByClienteId(clienteId).orElseThrow(() -> new ValidationException("Usuario não possui carrinho"));
	}
	
	@Transactional
	public Carrinho adicionarProdutosNoCarrinhoPorIdToken(Long clienteId, ItemCarrinho adicionarItem) {
		if (adicionarItem.getQuantidade() <= 0) {
			throw new ValidationException("A quantity do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ adicionarItem.getQuantidade());
		}

		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);
		carrinho.setDataEHoraAtualizacao(LocalDateTime.now());

		List<ItemCarrinho> itemsCarrinho = carrinho.getItemsCarrinho();
		Optional<ItemCarrinho> optionalItemCarrinho = itemsCarrinho.stream().filter(item -> item.getId().equals(adicionarItem.getProdutoId())).findFirst();

		if (optionalItemCarrinho.isPresent()) {
			var itemCarrinho = optionalItemCarrinho.get();
			itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + adicionarItem.getQuantidade());
		} else {
			Product produto = produtoService.getProductById(adicionarItem.getProdutoId());
			BigDecimal subTotal = produto.getPrice().multiply(BigDecimal.valueOf(adicionarItem.getQuantidade()));
			ItemCarrinho itemCarrinho = ItemCarrinho.builder()
					.id(null)
					.quantidade(adicionarItem.getQuantidade())
					.produtoId(adicionarItem.getProdutoId())
					.subTotal(subTotal)
					.dataEHoraInsercao(LocalDateTime.now())
					.carrinho(carrinho).build();

			//No metodo adicionarItemCarrinho já faz a definição do novo valor total na entidade Carrinho
			carrinho.adicionarItemCarrinho(itemCarrinho);
		}

	    return carrinhoRepository.save(carrinho);
	}

	@Transactional
	public void setarQuantidadeProdutoNoCarrinho(Long clienteId, ItemCarrinho setarItem) {
		Carrinho carrinho = pegarCarrinhoPorClienteId(clienteId);

		ItemCarrinho itemCarrinho = carrinho.getItemsCarrinho().stream().filter(item -> item.getProdutoId().equals(setarItem.getProdutoId()))
				.findFirst().orElseThrow(() -> new ValidationException("Esse produto não foi adicionado ao carrinho ainda"));

		if (setarItem.getQuantidade() <= 0) {
			carrinho.removerItemCarrinho(itemCarrinho);
		} else {
			itemCarrinho.setQuantidade(setarItem.getQuantidade());
		}

		carrinhoRepository.save(carrinho);
	}
}
