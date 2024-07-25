package br.com.danielschiavo.customer.dto.response.card;

import br.com.danielschiavo.customer.model.enums.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailCardResponse(
        Long id,
        @JsonProperty("bank_name")
        String bankName,
        @JsonProperty("card_number")
        String cardNumber,
        @JsonProperty("name_on_card")
        String nameOnCard,
        @JsonProperty("expiration_date")
        String expirationDate,
        @JsonProperty("is_default")
        Boolean isDefault,
        @JsonProperty("card_type")
        CardType cardType
) {
}
