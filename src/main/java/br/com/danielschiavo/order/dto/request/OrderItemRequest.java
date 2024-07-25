package br.com.danielschiavo.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderItemRequest(
			@NotNull
			@JsonProperty("product_id")
			Long productId,
			@NotNull
			Integer quantity
		) {

}
