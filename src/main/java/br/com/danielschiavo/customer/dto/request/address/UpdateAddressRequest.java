package br.com.danielschiavo.customer.dto.request.address;

import lombok.Builder;

@Builder
public record UpdateAddressRequest(
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
