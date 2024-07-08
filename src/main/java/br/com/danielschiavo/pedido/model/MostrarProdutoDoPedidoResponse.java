package br.com.danielschiavo.pedido.model;

import java.math.BigDecimal;

import br.com.danielschiavo.pedido.model.itempedido.ItemPedido;
import lombok.Builder;

@Builder
public record MostrarProdutoDoPedidoResponse(
		Long idProduto,
		String nomeProduto,
		BigDecimal preco,
		Integer quantidade,
		BigDecimal subTotal,
		byte[] primeiraImagem
		) {

	public MostrarProdutoDoPedidoResponse(ItemPedido itemPedido, byte[] primeiraImagem) {
		this(itemPedido.getProdutoId(),
			 itemPedido.getNomeProduto(),
			 itemPedido.getPreco(),
			 itemPedido.getQuantidade(),
			 itemPedido.getSubTotal(),
			 primeiraImagem);
	}

}
