package br.com.danielschiavo.pedido.model.itempedido;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AdicionarItemPedidoDTO(
			@NotNull
			Long produtoId,
			@NotNull
			Integer quantidade
		) {

}
