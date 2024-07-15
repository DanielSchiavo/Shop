package br.com.danielschiavo.order.model.enums;

import br.com.danielschiavo.order.service.payment.processor.PaymentProcessor;
import br.com.danielschiavo.order.service.payment.processor.ProcessBoletoPayment;
import br.com.danielschiavo.order.service.payment.processor.ProcessCreditCardPayment;
import br.com.danielschiavo.order.service.payment.processor.ProcessDebitCardPayment;
import br.com.danielschiavo.order.service.payment.processor.ProcessPixPayment;

public enum PaymentMethod {
	CREDIT_CARD(new ProcessCreditCardPayment())
	{
		@Override
		public boolean needsCard() {
			return true;
		}

		@Override
		public boolean canBeInInstallments() {
			return true;
		}

		@Override
		public PaymentStatus paymentStatusShouldBe() {
			return PaymentStatus.IN_PROCESSING;
		}
	},
 	DEBIT_CARD(new ProcessDebitCardPayment())
 	{
		@Override
		public boolean needsCard() {
			return true;
		}

		@Override
		public boolean canBeInInstallments() {
			return false;
		}

		@Override
		public PaymentStatus paymentStatusShouldBe() {
			return PaymentStatus.IN_PROCESSING;
		}
	},
 	PIX(new ProcessPixPayment())
 	{
		@Override
		public boolean needsCard() {
			return false;
		}

		@Override
		public boolean canBeInInstallments() {
			return false;
		}

		@Override
		public PaymentStatus paymentStatusShouldBe() {
			return PaymentStatus.PENDING;
		}
	},
 	BOLETO(new ProcessBoletoPayment()) {
		@Override
		public boolean needsCard() {
			return false;
		}

		@Override
		public boolean canBeInInstallments() {
			return false;
		}

		@Override
		public PaymentStatus paymentStatusShouldBe() {
			return PaymentStatus.PENDING;
		}
	};
	
	private PaymentProcessor paymentProcessor;
	
	PaymentMethod(PaymentProcessor paymentProcessor){
		this.paymentProcessor = paymentProcessor;
	}
	
	public PaymentProcessor getProcessor() {
		return this.paymentProcessor;
	}
	
	public abstract boolean needsCard();
	
	public abstract boolean canBeInInstallments();
	
	public abstract PaymentStatus paymentStatusShouldBe();
}
