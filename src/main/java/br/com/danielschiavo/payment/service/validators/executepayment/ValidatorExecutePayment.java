package br.com.danielschiavo.payment.service.validators.executepayment;

import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;

public interface ValidatorExecutePayment {

    public void validate(AddPaymentRequest request);
}
