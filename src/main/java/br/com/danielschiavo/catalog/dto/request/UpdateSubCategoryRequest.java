package br.com.danielschiavo.catalog.dto.request;

public record UpdateSubCategoryRequest(
		String name,
		Long categoryId
		) {

}
