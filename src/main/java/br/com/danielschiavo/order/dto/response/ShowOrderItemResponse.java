package br.com.danielschiavo.order.dto.response;

import java.math.BigDecimal;

import br.com.danielschiavo.order.model.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ShowOrderItemResponse(
		@JsonProperty("product_id")
		Long productId,
		@JsonProperty("product_name")
		String productName,
		BigDecimal price,
		Integer quantity,
		@JsonProperty("sub_total")
		BigDecimal subTotal,
		@JsonProperty("first_image")
		byte[] firstImage
		) {

	public ShowOrderItemResponse(OrderItem orderItem, byte[] firstImage) {
		this(orderItem.getProductId(),
			 orderItem.getName(),
			 orderItem.getPrice(),
			 orderItem.getQuantity(),
			 orderItem.getSubTotal(),
			 firstImage);
	}

}
