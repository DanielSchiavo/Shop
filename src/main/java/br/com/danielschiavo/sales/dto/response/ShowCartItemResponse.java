package br.com.danielschiavo.sales.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ShowCartItemResponse(
		@JsonProperty("product_id")
		Long productId,
		Integer quantity,
		@JsonProperty("sub_total")
		BigDecimal subTotal
		) {

}
