package br.com.danielschiavo.order.service.delivery.processor;

import br.com.danielschiavo.customer.model.entity.Customer;

public class ProcessDigitalDelivery extends DeliveryProcessor {

	@Override
	public boolean execute(Customer customer) {
		System.out.println("Processing digital delivery to the customer " + customer.getName());
		return true;
	}

}
