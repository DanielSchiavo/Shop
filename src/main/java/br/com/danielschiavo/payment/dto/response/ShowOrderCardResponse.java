package br.com.danielschiavo.payment.dto.response;

import br.com.danielschiavo.customer.model.enums.CardType;
import br.com.danielschiavo.payment.model.valueobject.PaymentCard;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShowOrderCardResponse(
		@JsonProperty("bank_name")
		String bankName,
		@JsonProperty("card_number")
		String cardNumber,
		@JsonProperty("name_on_card")
		String nameOnCard,
		@JsonProperty("number_of_installments")
		Byte numberOfInstallments,
		@JsonProperty("card_type")
		CardType cardType
		) {
	
    public ShowOrderCardResponse(PaymentCard paymentCard) {
        this(
        		paymentCard.getBankName(),
        		paymentCard.getCardNumber(),
        		paymentCard.getNameOnCard(),
        		paymentCard.getNumberOfInstallments(),
        		paymentCard.getCardType()
        );
    }
}
