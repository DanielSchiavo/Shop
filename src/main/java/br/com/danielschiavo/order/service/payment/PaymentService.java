package br.com.danielschiavo.pedido.service.payment;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.pedido.model.enums.PaymentStatus;
import br.com.danielschiavo.pedido.model.valueobject.OrderCard;
import br.com.danielschiavo.pedido.model.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private CardService cartaoService;

    public Payment execute(Payment payment, BigDecimal valorTotal, Customer customer) {
        payment.setPaymentStatus(PaymentStatus.PENDING);
        if (payment.getOrderCard() != null && payment.getOrderCard().getCardId() != null) {
            var card = cartaoService.getCardByIdAndCustomerId(payment.getOrderCard().getCardId(), customer.getId());

            OrderCard orderCard = payment.getOrderCard();
            orderCard.setBankName(card.getBankName());
            orderCard.setCardNumber(card.getCardNumber());
            orderCard.setNameOnCard(card.getNameOnCard());
            orderCard.setExpirationDate(card.getExpirationDate());
            orderCard.setCardType(card.getCardType());
        }

        payment.getPaymentMethod().getProcessor().execute(customer, valorTotal);

        return payment;
    }
}
