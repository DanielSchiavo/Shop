package br.com.danielschiavo.order.service.payment.processor;


import br.com.danielschiavo.customer.model.entity.Customer;

import java.math.BigDecimal;

public class ProcessPixPayment extends PaymentProcessor {
	
	@Override
	public boolean execute(Customer customer, BigDecimal totalValue) {
		System.out.println("Generating QR Code and key copy and paste from Pix to customer " + customer.getName() + " he bought R$" + totalValue + " in products");
		return true;
	}

}
