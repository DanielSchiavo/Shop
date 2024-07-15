package br.com.danielschiavo.pedido.dto.request.orderitem;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderItemRequest(
			@NotNull
			Long productId,
			@NotNull
			Integer quantity
		) {

}
