package br.com.danielschiavo.payment.dto.response;

import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ShowPaymentResponse(
		@JsonProperty("payment_method")
		PaymentMethod paymentMethod,
		@JsonProperty("payment_status")
		PaymentStatus paymentStatus,
		@JsonProperty("order_card")
		ShowOrderCardResponse orderCard
		) {

}
