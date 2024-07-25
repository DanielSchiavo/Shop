package br.com.danielschiavo.customer.dto.response.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@JsonInclude(Include.NON_NULL)
public record DetailCustomerResponse (
		Long id,
		String cpf,
		String name,
		String surname,
		@JsonProperty("birth_date")
		LocalDate birthDate,
		@JsonProperty("account_creation_date")
		LocalDate accountCreationDate,
		String email,
		@JsonProperty("cellphone_number")
		String cellphoneNumber,
		@JsonProperty("profile_picture")
		String profilePicture
) {
	

}
