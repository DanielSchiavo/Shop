package br.com.danielschiavo.pedido.service.delivery.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DeliveryProcessor {
	
	public abstract boolean execute(Customer customer);

}
