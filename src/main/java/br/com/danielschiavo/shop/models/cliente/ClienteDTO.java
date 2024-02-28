package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteDTO(
		@NotBlank
		String cpf,
		@NotBlank
		String nome,
		String sobrenome,
		@NotNull
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
		LocalDate data_nascimento,
		@NotBlank
		@Email
		String email,
		String senha,
		@NotBlank
		String celular,
		MostrarEnderecoDTO endereco	
		) {
}
