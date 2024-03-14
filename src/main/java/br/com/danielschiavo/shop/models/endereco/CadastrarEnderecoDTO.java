package br.com.danielschiavo.shop.models.endereco;

import jakarta.validation.constraints.NotBlank;

public record CadastrarEnderecoDTO(
		@NotBlank
		String cep,
		@NotBlank
		String rua,
		@NotBlank
		String numero,
		String complemento,
		@NotBlank
		String bairro,
		@NotBlank
		String cidade,
		@NotBlank
		String estado,
		@NotBlank
		Boolean enderecoPadrao
		) {

}
