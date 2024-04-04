package br.com.danielschiavo.shop.models.cliente.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CadastrarEnderecoDTO(
		@NotBlank
		@Size(min=8, max=8)
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
		@Size(min=2, max=2)
		String estado,
		@NotNull
		Boolean enderecoPadrao
		) {

}
