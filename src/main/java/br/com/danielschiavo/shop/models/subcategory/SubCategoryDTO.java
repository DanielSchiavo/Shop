package br.com.danielschiavo.shop.models.subcategory;

import org.springframework.format.annotation.NumberFormat;

import br.com.danielschiavo.shop.models.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubCategoryDTO(
		@NotNull
		@NumberFormat
		Long id,
		@NotBlank
		String name,
		@NotNull
		Category category
		) {

}
