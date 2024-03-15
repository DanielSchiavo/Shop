package br.com.danielschiavo.shop.models.cartao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarCartaoDTO(
		@NotBlank
		String validadeCartao,
		@NotBlank
		String numeroCartao,
		@NotBlank
		String nomeNoCartao,
		@NotNull
		Boolean cartaoPadrao,
		@NotNull
		TipoCartao tipoCartao
		) {

}
