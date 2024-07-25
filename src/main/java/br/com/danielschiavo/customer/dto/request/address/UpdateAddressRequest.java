package br.com.danielschiavo.customer.dto.request.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UpdateAddressRequest(
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
