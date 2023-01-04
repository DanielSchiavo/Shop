package br.com.danielschiavo.shop.controllers;

import br.com.danielschiavo.shop.models.category.Category;

public record UpdateSubCategoryDTO(
		String name,
		Category category
		) {

}
