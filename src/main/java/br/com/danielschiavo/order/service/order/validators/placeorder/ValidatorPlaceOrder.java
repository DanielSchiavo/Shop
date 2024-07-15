package br.com.danielschiavo.pedido.service.order.validators.placeorder;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.pedido.model.entity.Order;

public interface ValidatorPlaceOrder {
	
	void validate(Order order, Customer customer);
	
}
