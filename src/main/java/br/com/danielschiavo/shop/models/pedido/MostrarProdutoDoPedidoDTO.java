package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;

public record MostrarProdutoDoPedidoDTO(
		String nomeProduto,
		BigDecimal preco,
		Integer quantidade,
		byte[] primeiraImagem
		) {

	public MostrarProdutoDoPedidoDTO(ItemPedido itemPedido, byte[] primeiraImagem) {
		this(itemPedido.getNomeProduto(),
			 itemPedido.getPreco(),
			 itemPedido.getQuantidade(),
			 primeiraImagem);
	}

}
