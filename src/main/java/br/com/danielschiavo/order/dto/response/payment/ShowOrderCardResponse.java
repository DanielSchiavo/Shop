package br.com.danielschiavo.order.dto.response.payment;

import br.com.danielschiavo.customer.model.enums.CardType;
import br.com.danielschiavo.order.model.valueobject.OrderCard;

public record ShowOrderCardResponse(
		String bankName,
		String cardNumber,
		String nameOnCard,
		Byte numberOfInstallments,
		CardType cardType
		) {
	
    public ShowOrderCardResponse(OrderCard orderCard) {
        this(
        		orderCard.getBankName(),
        		orderCard.getCardNumber(),
        		orderCard.getNameOnCard(),
        		orderCard.getNumberOfInstallments(),
        		orderCard.getCardType()
        );
    }
}
