package br.com.danielschiavo.cliente.dto.request.cliente;

import java.time.LocalDate;

import br.com.danielschiavo.cliente.dto.request.endereco.CadastrarEnderecoRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CadastrarClienteRequest(
		@NotBlank
		@Size(min=11, max=11)
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
		@Size(min=11, max=11)
		String celular,
		String fotoPerfil,
		CadastrarEnderecoRequest endereco
		) {
}
