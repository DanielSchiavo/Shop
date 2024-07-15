package br.com.danielschiavo.order.service.delivery.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.order.service.delivery.processor.enums.CorreioService;

public class ProcessCorreiosDelivery extends DeliveryProcessor {

	private CorreioService correioService;

	public ProcessCorreiosDelivery(CorreioService correioService) {
		this.correioService = correioService;
	}

	@Override
	public boolean execute(Customer customer) {
		System.out.println("Processing delivery via correios to the customer " + customer.getName());

		switch (correioService) {
			case CorreioService.PAC:
				System.out.println("Processing pac");
				break;
			case CorreioService.SEDEX:
				System.out.println("Processing sedex");
				break;
		}

		return false;
	}

}
