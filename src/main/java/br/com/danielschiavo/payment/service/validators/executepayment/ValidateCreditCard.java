package br.com.danielschiavo.payment.service.validators.executepayment;

import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class ValidateCreditCard implements ValidatorExecutePayment {

    @Override
    public void validate(AddPaymentRequest request) {
        boolean isCreditCard = request.paymentMethod() == PaymentMethod.CREDIT_CARD;
        if (isCreditCard && request.cardId() == null || request.numberOfInstallments() == null) {
            throw new ValidationException("When payment method is a credit card you must inform the ID of the card that will be used and the number of installments");
        }
    }
}
