package br.com.danielschiavo.catalog.dto.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
		@NotBlank
		@NotNull
		String name,
		String description,
		String image,
		@JsonProperty("parent_category_id")
		Long parentCategoryId
		) {
}
