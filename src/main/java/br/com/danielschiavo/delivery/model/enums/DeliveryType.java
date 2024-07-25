package br.com.danielschiavo.delivery.model.enums;

import br.com.danielschiavo.delivery.service.processor.DeliveryProcessor;
import br.com.danielschiavo.delivery.service.processor.ProcessCorreiosDelivery;
import br.com.danielschiavo.delivery.service.processor.ProcessDigitalDelivery;
import br.com.danielschiavo.delivery.service.processor.ProcessExpressDelivery;
import br.com.danielschiavo.delivery.service.processor.ProcessPickUpInStoreDelivery;
import br.com.danielschiavo.delivery.service.processor.enums.CorreioService;

public enum DeliveryType {
	CORREIOS_SEDEX(new ProcessCorreiosDelivery(CorreioService.SEDEX)),
	CORREIOS_PAC(new ProcessCorreiosDelivery(CorreioService.PAC)),
 	EXPRESS(new ProcessExpressDelivery()),
 	PICK_UP_IN_STORE(new ProcessPickUpInStoreDelivery()),
 	DIGITAL(new ProcessDigitalDelivery());
	
	private final DeliveryProcessor deliveryProcessor;
	
	DeliveryType(DeliveryProcessor deliveryProcessor){
		this.deliveryProcessor = deliveryProcessor;
	}
	
	public DeliveryProcessor getProcessor() {
		return this.deliveryProcessor;
	}
}
