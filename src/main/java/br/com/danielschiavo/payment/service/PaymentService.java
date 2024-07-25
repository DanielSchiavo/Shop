package br.com.danielschiavo.payment.service;

import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.payment.mapper.PaymentMapper;
import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import br.com.danielschiavo.payment.model.valueobject.PaymentCard;
import br.com.danielschiavo.payment.model.entity.Payment;
import br.com.danielschiavo.payment.repository.PaymentRepository;
import br.com.danielschiavo.payment.service.validators.executepayment.ValidatorExecutePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentMapper mapper;

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private List<ValidatorExecutePayment> validators;

    public ShowPaymentResponse executePayment(AddPaymentRequest request, DetailOrderResponse order, DetailCardResponse card) {
        validators.forEach(v -> v.validate(request));

        Payment payment = mapper.toEntity(request);

        payment.setPaymentStatus(PaymentStatus.PENDING);

        if (request.paymentMethod() == PaymentMethod.CREDIT_CARD || request.paymentMethod() == PaymentMethod.DEBIT_CARD) {
            PaymentCard paymentCard = new PaymentCard();
            paymentCard.setCardId(card.id());
            paymentCard.setBankName(card.bankName());
            paymentCard.setCardNumber(card.cardNumber());
            paymentCard.setNameOnCard(card.nameOnCard());
            paymentCard.setExpirationDate(card.expirationDate());
            paymentCard.setNumberOfInstallments(request.numberOfInstallments());
            paymentCard.setCardType(card.cardType());

            payment.setPaymentCard(paymentCard);
        }

        payment.getPaymentMethod().getProcessor().execute(order.getTotalValue(), payment);

        return mapper.toDto(repository.save(payment));
    }
}
