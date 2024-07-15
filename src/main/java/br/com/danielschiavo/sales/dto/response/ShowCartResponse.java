package br.com.danielschiavo.sales.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ShowCartResponse(
		Long customerId,
		List<ShowCartItemResponse> cartItems,
		BigDecimal totalValue
) {


}
