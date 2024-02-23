package br.com.danielschiavo.shop.models.categoria;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaDTO(
		
		@JsonProperty("nome")
		@NotBlank
		@NotNull
		String nome
		) {
}
