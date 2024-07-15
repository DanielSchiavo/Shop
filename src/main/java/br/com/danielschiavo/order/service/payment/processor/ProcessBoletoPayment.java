package br.com.danielschiavo.pedido.service.payment.processor;


import br.com.danielschiavo.customer.model.entity.Customer;

import java.math.BigDecimal;

public class ProcessBoletoPayment extends PaymentProcessor {

	@Override
	public boolean execute(Customer customer, BigDecimal totalValue) {
		System.out.println("Generating boleto to customer " + customer.getName() + " he bought R$" + totalValue + " in products" );
		return true;
	}

}
