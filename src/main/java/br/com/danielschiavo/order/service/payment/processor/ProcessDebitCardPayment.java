package br.com.danielschiavo.order.service.payment.processor;


import br.com.danielschiavo.customer.model.entity.Customer;

import java.math.BigDecimal;

public class ProcessDebitCardPayment extends PaymentProcessor {

	public boolean execute(Customer customer, BigDecimal totalValue) {
		System.out.println("Processing payment in debit card for customer " + customer.getName() + " he bought R$" + totalValue + " in products");
		return true;
	}


}
