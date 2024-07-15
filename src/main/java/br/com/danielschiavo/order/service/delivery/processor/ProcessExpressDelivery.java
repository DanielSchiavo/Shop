package br.com.danielschiavo.pedido.service.delivery.processor;

import br.com.danielschiavo.customer.model.entity.Customer;

public class ProcessExpressDelivery extends DeliveryProcessor {

	@Override
	public boolean execute(Customer customer) {
		System.out.println("Processing express delivery to the customer " + customer.getName());
		return true;
	}

}
