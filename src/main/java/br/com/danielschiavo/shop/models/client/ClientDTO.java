package br.com.danielschiavo.shop.models.client;

import java.time.LocalDate;

import br.com.danielschiavo.shop.models.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientDTO(
		@NotBlank
		String cpf,
		@NotBlank
		String name,
		@NotBlank
		String lastName,
		@NotNull
		LocalDate birthDate,
		@NotBlank
		@Email
		String email,
		@NotBlank
		String password,
		@NotBlank
		String telephone,
		Address address	
		) {
	
	public ClientDTO(Client client) {
		this(
			client.getCpf(),
			client.getName(),
			client.getLastName(),
			client.getBirthDate(),
			client.getEmail(),
			client.getPassword(),
			client.getTelephone(),
			client.getAddress()
				);
		
	}

}
