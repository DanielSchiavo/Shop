package br.com.danielschiavo.payment.dto.request;

import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AddPaymentRequest(
				@NotNull
				@JsonProperty("payment_method")
				PaymentMethod paymentMethod,
				@JsonProperty("card_id")
				Long cardId,
				@JsonProperty("number_of_installments")
				Byte numberOfInstallments
		) {

}
