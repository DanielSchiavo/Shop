package br.com.danielschiavo.payment.service.processor;


import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.payment.model.entity.Payment;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;

import java.math.BigDecimal;

public class ProcessPixPayment extends PaymentProcessor {
	
	@Override
	public void execute(BigDecimal totalValue, Payment payment) {
		System.out.println("Generating QR Code and key copy and paste from Pix to customer he bought R$" + totalValue + " in products");

		payment.setPaymentStatus(PaymentStatus.APPROVED_NOT_INTEGRATED);
	}

}
