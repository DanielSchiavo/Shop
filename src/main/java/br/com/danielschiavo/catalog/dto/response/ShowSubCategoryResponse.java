package br.com.danielschiavo.catalog.dto.response;


public record ShowSubCategoryResponse(
			Long id,
			String nome,
			Long categoryId
		) {
}
