package br.com.danielschiavo.pedido.model.enums;

import br.com.danielschiavo.pedido.service.delivery.processor.DeliveryProcessor;
import br.com.danielschiavo.pedido.service.delivery.processor.ProcessCorreiosDelivery;
import br.com.danielschiavo.pedido.service.delivery.processor.ProcessDigitalDelivery;
import br.com.danielschiavo.pedido.service.delivery.processor.ProcessExpressDelivery;
import br.com.danielschiavo.pedido.service.delivery.processor.ProcessPickUpInStoreDelivery;
import br.com.danielschiavo.pedido.service.delivery.processor.enums.CorreioService;

public enum DeliveryType {
	CORREIOS_SEDEX(new ProcessCorreiosDelivery(CorreioService.SEDEX))
	{
		@Override
		public boolean needsAddress() {
			return true;
		}
	},
	CORREIOS_PAC(new ProcessCorreiosDelivery(CorreioService.PAC))
	{
		@Override
		public boolean needsAddress() {
			return true;
		}
	},
 	EXPRESS(new ProcessExpressDelivery())
 	{
		@Override
		public boolean needsAddress() {
			return true;
		}
	},
 	PICK_UP_IN_STORE(new ProcessPickUpInStoreDelivery())
 	{
		@Override
		public boolean needsAddress() {
			return false;
		}
	},
 	DIGITAL(new ProcessDigitalDelivery())
 	{
		@Override
		public boolean needsAddress() {
			return false;
		}
	};
	
	private DeliveryProcessor deliveryProcessor;
	
	DeliveryType(DeliveryProcessor deliveryProcessor){
		this.deliveryProcessor = deliveryProcessor;
	}
	
	public DeliveryProcessor getProcessor() {
		return this.deliveryProcessor;
	}
	
	public abstract boolean needsAddress();
}
