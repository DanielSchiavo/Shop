package br.com.danielschiavo.produto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
		@NotBlank
		@NotNull
		String name
		) {
}
