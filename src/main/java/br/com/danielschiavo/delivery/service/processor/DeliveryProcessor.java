package br.com.danielschiavo.delivery.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.delivery.model.entity.Delivery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DeliveryProcessor {
	
	public abstract void execute(Delivery delivery);

}
