package br.com.danielschiavo.order.service.delivery.processor;

import br.com.danielschiavo.customer.model.entity.Customer;

public class ProcessPickUpInStoreDelivery extends DeliveryProcessor {

	@Override
	public boolean execute(Customer customer) {
		System.out.println("Processing pick up in store delivery to the customer " + customer.getName());
		return true;
	}

}
