package br.com.danielschiavo.pedido.service.payment.processor;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProcessCreditCardPayment extends PaymentProcessor {

	@Override
	public boolean execute(Customer customer, BigDecimal totalValue) {
		System.out.println("Processing payment in credit card for customer " + customer.getName() + " he bought R$" + totalValue + " in products");
		return true;
	}

}
