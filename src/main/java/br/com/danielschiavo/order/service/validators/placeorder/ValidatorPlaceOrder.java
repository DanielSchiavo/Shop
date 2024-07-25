package br.com.danielschiavo.order.service.validators.placeorder;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.order.model.entity.Order;

public interface ValidatorPlaceOrder {
	
	void validate(Order order, Customer customer);
	
}
