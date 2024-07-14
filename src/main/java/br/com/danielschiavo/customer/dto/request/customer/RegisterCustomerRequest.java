package br.com.danielschiavo.customer.dto.request.customer;

import java.time.LocalDate;

import br.com.danielschiavo.customer.dto.request.address.RegisterAddressRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterCustomerRequest(
		@NotBlank
		@Size(min=11, max=11)
		String cpf,
		@NotBlank
		String name,
		String surname,
		@NotNull
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
		@Past
		LocalDate birthDate,
		@NotBlank
		@Email
		String email,
		@NotBlank
		String password,
		@NotBlank
		@Size(min=11, max=11)
		String cellphoneNumber,
		String profilePicture,
		RegisterAddressRequest address
		) {
}
