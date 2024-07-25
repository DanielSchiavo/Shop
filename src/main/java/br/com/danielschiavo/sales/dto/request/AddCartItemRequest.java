package br.com.danielschiavo.sales.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AddCartItemRequest(
		@NotNull
		@NumberFormat
		@Positive
		@JsonProperty("product_id")
		Long productId,
		@NotNull
		@NumberFormat
		Integer quantity
		) {

}
