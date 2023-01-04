package br.com.danielschiavo.shop.models.client;

import java.time.LocalDate;

import br.com.danielschiavo.shop.models.address.Address;

public record DetailingClientDTO(
		String cpf,
		String name,
		String lastName,
		LocalDate birthDate,
		String email,
		String password,
		String telephone,
		Address address
		) {

	public DetailingClientDTO(Client client) {
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
