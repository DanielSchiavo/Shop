package br.com.danielschiavo.shop.models.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCategoriaDTO(
		
		@NotBlank
		@NotNull
		String nome
		) {
}
