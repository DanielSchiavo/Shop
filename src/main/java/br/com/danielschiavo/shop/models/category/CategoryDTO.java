package br.com.danielschiavo.shop.models.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank String name) {
	
	public CategoryDTO(Category category) {
		this(category.getName());
	}

}
