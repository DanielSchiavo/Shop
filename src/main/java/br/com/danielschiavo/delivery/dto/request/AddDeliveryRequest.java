package br.com.danielschiavo.delivery.dto.request;

import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AddDeliveryRequest(
			@NotNull
			@JsonProperty("delivery_type")
			DeliveryType deliveryType,
			@JsonProperty("address_id")
			Long addressId,
			@JsonProperty("order_id")
			UUID orderId
		) {

}
