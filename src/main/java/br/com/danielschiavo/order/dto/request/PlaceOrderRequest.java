package br.com.danielschiavo.order.dto.request;

import java.util.List;

import br.com.danielschiavo.delivery.dto.request.AddDeliveryRequest;
import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PlaceOrderRequest(
			@NotNull
            AddPaymentRequest payment,
			@NotNull
			AddDeliveryRequest delivery,
			@NotNull
			@JsonProperty("purchased_via_cart")
			Boolean purchasedViaCart,
			@NotNull
			List<OrderItemRequest> items
		) {

}
