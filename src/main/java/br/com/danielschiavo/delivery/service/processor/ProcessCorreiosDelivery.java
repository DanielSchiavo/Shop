package br.com.danielschiavo.delivery.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.delivery.model.entity.Delivery;
import br.com.danielschiavo.delivery.service.processor.enums.CorreioService;

public class ProcessCorreiosDelivery extends DeliveryProcessor {

	private CorreioService correioService;

	public ProcessCorreiosDelivery(CorreioService correioService) {
		this.correioService = correioService;
	}

	@Override
	public void execute(Delivery delivery) {
		System.out.println("Processing delivery via correios to the customer ");

		switch (correioService) {
			case CorreioService.PAC:
				System.out.println("Processing pac");
				break;
			case CorreioService.SEDEX:
				System.out.println("Processing sedex");
				break;
		}
	}

}
