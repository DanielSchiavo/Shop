package br.com.danielschiavo.order.dto.request.orderitem;

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
