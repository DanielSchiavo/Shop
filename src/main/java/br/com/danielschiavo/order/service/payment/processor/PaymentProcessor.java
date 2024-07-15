package br.com.danielschiavo.pedido.service.payment.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class PaymentProcessor {

	public abstract boolean execute(Customer customer, BigDecimal totalValue);
	
}
