package br.com.danielschiavo.order.service.order.validators.placeorder;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.model.enums.PaymentMethod;
import br.com.danielschiavo.order.model.valueobject.OrderCard;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatePaymentMethod implements ValidatorPlaceOrder {
	
	@Autowired
	private CardService cardService;
	
	@Override
	public void validate(Order order, Customer customer) {
		PaymentMethod paymentMethod = order.getPayment().getPaymentMethod();
		OrderCard orderCard = order.getPayment().getOrderCard();
		Long cardId = orderCard.getCardId();

		if ((paymentMethod.needsCard() && cardId == null)) {
			throw new ValidationException("The chosen payment method was " + paymentMethod + ", therefore, you must provide the customer's cardId and numberOfInstallments.");
		}
		if ((!paymentMethod.needsCard() && cardId != null)) {
			throw new ValidationException("The chosen payment method was " + paymentMethod + ", therefore, you should not provide a cardId or numberOfInstallments.");
		}
		
		if (cardId != null) {
			Card card = cardService.getCardByIdAndCustomerId(cardId, customer.getId());
			if (!paymentMethod.toString().endsWith(card.getCardType().toString())) {
				throw new ValidationException("The registered card with id number " + card.getId() + " is registered as a " + card.getCardType().toString() + " card, which does not match the provided payment method: " + paymentMethod.toString());
			}
		}
	}
}
