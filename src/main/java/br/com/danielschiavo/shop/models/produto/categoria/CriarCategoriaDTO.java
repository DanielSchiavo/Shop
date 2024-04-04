package br.com.danielschiavo.shop.models.produto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCategoriaDTO(
		
		@NotBlank
		@NotNull
		String nome
		) {
}
