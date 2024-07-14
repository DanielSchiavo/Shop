package br.com.danielschiavo.customer.dto.request.card;

import br.com.danielschiavo.customer.model.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterCardRequest(
		@NotBlank
		@Size(min=5, max=5)
		String expirationDate,
		@NotBlank
		@Size(min=16, max=16)
		String cardNumber,
		@NotBlank
		String nameOnCard,
		@NotNull
		Boolean isDefault,
		@NotNull
		CardType cardType
		) {

}
