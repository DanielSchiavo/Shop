package br.com.danielschiavo.sales.dto.request;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemRequest(
		@NotNull
		@NumberFormat
		@Positive
		Long productId,
		@NotNull
		@NumberFormat
		Integer quantity
		) {

}
