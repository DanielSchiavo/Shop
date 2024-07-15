package br.com.danielschiavo.produto.dto.response;


public record ShowSubCategoryResponse(
			Long id,
			String nome,
			Long categoryId
		) {
}
