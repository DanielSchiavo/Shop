package br.com.danielschiavo.order.dto.request.order;

import java.util.List;

import br.com.danielschiavo.order.dto.request.delivery.OrderDeliveryRequest;
import br.com.danielschiavo.order.dto.request.orderitem.OrderItemRequest;
import br.com.danielschiavo.order.dto.request.payment.OrderPaymentRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PlaceOrderRequest(
			@NotNull
			OrderPaymentRequest payment,
			@NotNull
			OrderDeliveryRequest delivery,
			@NotNull
			Boolean purchasedViaCart,
			@NotNull
			List<OrderItemRequest> items
		) {

}
