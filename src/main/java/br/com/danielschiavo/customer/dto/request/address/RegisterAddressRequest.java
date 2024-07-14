package br.com.danielschiavo.customer.dto.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterAddressRequest(
		@NotBlank
		@Size(min=8, max=8)
		String postalCode,
		@NotBlank
		String street,
		@NotBlank
		String number,
		String complement,
		@NotBlank
		String neighborhood,
		@NotBlank
		String city,
		@NotBlank
		@Size(min=2, max=2)
		String state,
		@NotNull
		Boolean isDefault
		) {

}
