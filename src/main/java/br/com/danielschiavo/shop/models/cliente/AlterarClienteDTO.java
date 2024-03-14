package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;

public record AlterarClienteDTO(
		String cpf,
		String nome,
		String sobrenome,
		LocalDate dataNascimento,
		String email,
		String senha,
		String celular
		) {

}
