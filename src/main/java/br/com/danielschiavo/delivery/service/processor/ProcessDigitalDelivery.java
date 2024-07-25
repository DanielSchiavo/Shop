package br.com.danielschiavo.delivery.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.delivery.model.entity.Delivery;

public class ProcessDigitalDelivery extends DeliveryProcessor {

	@Override
	public void execute(Delivery delivery) {
		System.out.println("Processing digital delivery to the customer ");
	}

}
