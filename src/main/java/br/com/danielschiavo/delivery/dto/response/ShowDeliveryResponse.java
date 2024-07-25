package br.com.danielschiavo.delivery.dto.response;

import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ShowDeliveryResponse(
		Long id,
		@JsonProperty("delivery_type")
		DeliveryType deliveryType,
		@JsonProperty("order_address")
		ShowOrderAddressResponse deliveryAddress
		) {

}
