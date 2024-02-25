package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;

public record MostrarProdutoPedidosAConfirmarDTO(
		String nome_produto,
		BigDecimal preco,
		byte[] primeira_imagem
		) {

	public MostrarProdutoPedidosAConfirmarDTO(ItemPedido itemPedido) {
		this(
			itemPedido.getNome_produto(),
			itemPedido.getPreco(),
			null
				);
	}

}
