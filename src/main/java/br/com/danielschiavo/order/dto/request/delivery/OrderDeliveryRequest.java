package br.com.danielschiavo.order.dto.request.delivery;

import br.com.danielschiavo.order.model.enums.DeliveryType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderDeliveryRequest(
			@NotNull
			DeliveryType deliveryType,
			Long enderecoId
		) {

}
