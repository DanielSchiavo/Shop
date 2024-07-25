package br.com.danielschiavo.payment.service.validators.executepayment;

import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class ValidateDebitCard implements ValidatorExecutePayment {

    @Override
    public void validate(AddPaymentRequest request) {
        boolean isDebitCard = request.paymentMethod() == PaymentMethod.DEBIT_CARD;
        if (isDebitCard && request.cardId() == null) {
            throw new ValidationException("When payment method is a debit card you must inform the ID of the card that will be used");
        }
    }
}
