package br.com.danielschiavo.shop.models.client;

import java.time.LocalDate;

import br.com.danielschiavo.shop.models.address.Address;

public record UpdateClientDTO(
		String cpf,
		String name,
		String lastName,
		LocalDate birthDate,
		String email,
		String password,
		String telephone,
		Address address
		) {

}
