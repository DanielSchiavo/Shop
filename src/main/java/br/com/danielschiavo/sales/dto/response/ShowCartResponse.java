package br.com.danielschiavo.sales.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record ShowCartResponse(
		@JsonProperty("customer_id")
		Long customerId,
		@JsonProperty("cart_items")
		List<ShowCartItemResponse> cartItems,
		@JsonProperty("total_value")
		BigDecimal totalValue
) {


}
