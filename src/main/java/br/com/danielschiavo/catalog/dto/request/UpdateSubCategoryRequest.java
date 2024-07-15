package br.com.danielschiavo.produto.dto.request;

public record UpdateSubCategoryRequest(
		String name,
		Long categoryId
		) {

}
