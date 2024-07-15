package br.com.danielschiavo.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
		@NotBlank
		@NotNull
		String name
		) {
}
