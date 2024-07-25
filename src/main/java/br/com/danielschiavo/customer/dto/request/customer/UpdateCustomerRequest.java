package br.com.danielschiavo.customer.dto.request.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCustomerRequest(
		@Size(min=11, max=11)
		String cpf,
		String name,
		String surname,
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
		@Past
		@JsonProperty("birth_date")
		LocalDate birthDate,
		@Email
		String email,
		String password,
		@Size(min=11, max=11)
		@JsonProperty("cellphone_number")
		String cellphoneNumber
		) {

}
