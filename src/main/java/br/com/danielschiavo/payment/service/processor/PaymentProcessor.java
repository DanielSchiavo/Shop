package br.com.danielschiavo.payment.service.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.payment.model.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class PaymentProcessor {

	public abstract void execute(BigDecimal totalValue, Payment payment);
	
}
