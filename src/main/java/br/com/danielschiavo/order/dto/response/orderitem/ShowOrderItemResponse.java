package br.com.danielschiavo.pedido.dto.response.orderitem;

import java.math.BigDecimal;

import br.com.danielschiavo.pedido.model.entity.OrderItem;
import lombok.Builder;

@Builder
public record ShowOrderItemResponse(
		Long productId,
		String productName,
		BigDecimal price,
		Integer quantity,
		BigDecimal subTotal,
		byte[] firstImage
		) {

	public ShowOrderItemResponse(OrderItem orderItem, byte[] firstImage) {
		this(orderItem.getProductId(),
			 orderItem.getProductName(),
			 orderItem.getPrice(),
			 orderItem.getQuantity(),
			 orderItem.getSubTotal(),
			 firstImage);
	}

}
