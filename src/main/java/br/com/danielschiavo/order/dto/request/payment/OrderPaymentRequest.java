package br.com.danielschiavo.order.dto.request.payment;

import br.com.danielschiavo.order.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderPaymentRequest(
				@NotNull
				PaymentMethod paymentMethod,
				Long cardId,
				String numberOfInstallments
		) {

}
