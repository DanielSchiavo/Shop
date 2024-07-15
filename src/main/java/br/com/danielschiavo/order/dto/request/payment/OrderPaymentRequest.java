package br.com.danielschiavo.pedido.dto.request.payment;

import br.com.danielschiavo.pedido.model.enums.PaymentMethod;
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
