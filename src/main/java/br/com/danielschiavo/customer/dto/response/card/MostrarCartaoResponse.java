package br.com.danielschiavo.customer.dto.response.card;

import br.com.danielschiavo.customer.model.enums.CardType;
import lombok.Builder;

@Builder
public record MostrarCartaoResponse(
			Long id,
			String bankName,
			String cardNumber,
			String nameOnCard,
			String expirationDate,
			Boolean isDefault,
			CardType cardType
) {
}
