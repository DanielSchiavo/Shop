package br.com.danielschiavo.pedido.dto.response.payment;

import br.com.danielschiavo.pedido.model.enums.PaymentMethod;
import br.com.danielschiavo.pedido.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ShowOrderPaymentResponse(
		PaymentMethod paymentMethod,
		PaymentStatus paymentStatus,
		ShowOrderCardResponse orderCard
		) {

}
