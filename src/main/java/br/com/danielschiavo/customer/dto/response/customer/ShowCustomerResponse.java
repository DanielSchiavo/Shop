package br.com.danielschiavo.customer.dto.response.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ShowCustomerResponse {
	
	private Long id;
	private String cpf;
	private String name;
	private String surname;
	private LocalDate birthDate;
	private LocalDate accountCreationDate;
	private String email;
	private String cellphoneNumber;
	private String profilePicture;
}
