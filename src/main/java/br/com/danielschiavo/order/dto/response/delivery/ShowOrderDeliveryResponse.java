package br.com.danielschiavo.pedido.dto.response.delivery;

import br.com.danielschiavo.pedido.model.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ShowOrderDeliveryResponse(
		DeliveryType deliveryType,
		ShowOrderAddressResponse orderAddress
		) {

}
