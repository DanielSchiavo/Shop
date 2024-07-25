package br.com.danielschiavo.payment.model.valueobject;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.model.enums.CardType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentCard {

	private Long cardId;
	
	private String bankName;
	private String cardNumber;
	private String nameOnCard;
	private String expirationDate;
	private Byte numberOfInstallments;
	
	@Enumerated(EnumType.STRING)
	private CardType cardType;

}
