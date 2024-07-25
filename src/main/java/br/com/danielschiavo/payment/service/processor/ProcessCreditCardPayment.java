package br.com.danielschiavo.payment.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.payment.model.entity.Payment;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProcessCreditCardPayment extends PaymentProcessor {

	@Override
	public void execute(BigDecimal totalValue, Payment payment) {
		System.out.println("Processing payment in credit card for customer he bought R$" + totalValue + " in products");

		payment.setPaymentStatus(PaymentStatus.APPROVED_NOT_INTEGRATED);
	}

}
