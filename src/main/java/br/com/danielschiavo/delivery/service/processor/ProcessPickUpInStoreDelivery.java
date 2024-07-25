package br.com.danielschiavo.delivery.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.delivery.model.entity.Delivery;

public class ProcessPickUpInStoreDelivery extends DeliveryProcessor {

	@Override
	public void execute(Delivery delivery) {
		System.out.println("Processing pick up in store delivery to the customer");
	}

}
