package br.com.danielschiavo.payment.model.enums;

import br.com.danielschiavo.payment.service.processor.PaymentProcessor;
import br.com.danielschiavo.payment.service.processor.ProcessBoletoPayment;
import br.com.danielschiavo.payment.service.processor.ProcessCreditCardPayment;
import br.com.danielschiavo.payment.service.processor.ProcessDebitCardPayment;
import br.com.danielschiavo.payment.service.processor.ProcessPixPayment;

public enum PaymentMethod {
	CREDIT_CARD(new ProcessCreditCardPayment()),
 	DEBIT_CARD(new ProcessDebitCardPayment()),
 	PIX(new ProcessPixPayment()),
 	BOLETO(new ProcessBoletoPayment());
	
	private PaymentProcessor paymentProcessor;
	
	PaymentMethod(PaymentProcessor paymentProcessor){
		this.paymentProcessor = paymentProcessor;
	}
	
	public PaymentProcessor getProcessor() {
		return this.paymentProcessor;
	}
}
