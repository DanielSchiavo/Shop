package br.com.danielschiavo.pedido.dto.request.delivery;

import br.com.danielschiavo.pedido.model.enums.DeliveryType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderDeliveryRequest(
			@NotNull
			DeliveryType deliveryType,
			Long enderecoId
		) {

}
