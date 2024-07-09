package br.com.danielschiavo.pedido.dto.request.itempedido;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AdicionarItemPedidoRequest(
			@NotNull
			Long produtoId,
			@NotNull
			Integer quantidade
		) {

}
