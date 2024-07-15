package br.com.danielschiavo.catalog.dto.request;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSubCategoryRequest(
		@NotBlank
		String name,
		@NotNull
		@NumberFormat
		Long categoryId
		) {

}
