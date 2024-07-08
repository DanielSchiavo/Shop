package br.com.danielschiavo.produto.model.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCategoriaRequest(
		
		@NotBlank
		@NotNull
		String nome
		) {
}
