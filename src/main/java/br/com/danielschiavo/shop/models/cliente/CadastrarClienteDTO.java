package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record CadastrarClienteDTO(
		@NotBlank
		String cpf,
		@NotBlank
		String nome,
		String sobrenome,
		@NotNull
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
		@Past
		LocalDate dataNascimento,
		@NotBlank
		@Email
		String email,
		@NotBlank
		String senha,
		@NotBlank
		String celular,
		@NotBlank
		String fotoPerfil,
		CadastrarEnderecoDTO endereco
		) {
}
