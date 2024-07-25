package br.com.danielschiavo.customer.dto.response.address;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailAddressResponse(
        Long id,
        @JsonProperty("postal_code")
        String postalCode,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        @JsonProperty("is_default")
        Boolean isDefault
) {
}
