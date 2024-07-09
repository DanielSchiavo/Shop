package br.com.danielschiavo.pedido.dto.response.itempedido;

import java.math.BigDecimal;

import br.com.danielschiavo.pedido.model.entity.ItemPedido;
import lombok.Builder;

@Builder
public record MostrarItemPedidoResponse(
		Long idProduto,
		String nomeProduto,
		BigDecimal preco,
		Integer quantidade,
		BigDecimal subTotal,
		byte[] primeiraImagem
		) {

	public MostrarItemPedidoResponse(ItemPedido itemPedido, byte[] primeiraImagem) {
		this(itemPedido.getProdutoId(),
			 itemPedido.getNomeProduto(),
			 itemPedido.getPreco(),
			 itemPedido.getQuantidade(),
			 itemPedido.getSubTotal(),
			 primeiraImagem);
	}

}
