package br.com.danielschiavo.customer.dto.request.card;

import br.com.danielschiavo.customer.model.enums.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterCardRequest(
		@NotBlank
		@Size(min=5, max=5)
		@JsonProperty("expiration_date")
		String expirationDate,
		@NotBlank
		@Size(min=16, max=16)
		@JsonProperty("card_number")
		String cardNumber,
		@NotBlank
		@JsonProperty("name_on_card")
		String nameOnCard,
		@NotNull
		@JsonProperty("is_default")
		Boolean isDefault,
		@NotNull
		@JsonProperty("card_type")
		CardType cardType
		) {

}
