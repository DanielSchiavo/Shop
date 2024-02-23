package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;

public record MostrarProdutoPedidosAConfirmarDTO(
		Long produto_id,
		String nome_produto,
		BigDecimal preco,
		byte[] primeira_imagem
		) {

	public MostrarProdutoPedidosAConfirmarDTO(ItemPedido itemPedido) {
		this(
			itemPedido.getProduto_id(),
			itemPedido.getNome_produto(),
			itemPedido.getPreco(),
			null
				);
	}

}
