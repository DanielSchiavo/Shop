package br.com.danielschiavo.sales.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ShowCartItemResponse(
							Long productId,
							Integer quantity,
							BigDecimal subTotal
		) {

}
