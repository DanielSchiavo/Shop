package br.com.danielschiavo.customer.dto.response.address;

import lombok.Builder;

@Builder
public record ShowAddressResponse(
		Long id,
		String postalCode,
		String street,
		String number,
		String complement,
		String neighborhood,
		String city,
		String state,
		Boolean isDefault
		) {
}
