package br.com.danielschiavo.shop.models.pedido.itempedido;

import jakarta.validation.constraints.NotNull;

public record AdicionarItemPedidoDTO(
			@NotNull
			Long idProduto,
			@NotNull
			Integer quantidade
		) {

}
