package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record AlterarClienteDTO(
		@Size(min=11, max=11)
		String cpf,
		String nome,
		String sobrenome,
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
		@Past
		LocalDate dataNascimento,
		@Email
		String email,
		String senha,
		@Size(min=11, max=11)
		String celular
		) {

}
