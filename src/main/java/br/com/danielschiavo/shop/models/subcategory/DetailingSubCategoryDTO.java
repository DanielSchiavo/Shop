package br.com.danielschiavo.shop.models.subcategory;

import br.com.danielschiavo.shop.models.category.Category;

public record DetailingSubCategoryDTO(Long id, String name, Category category) {
	
	public DetailingSubCategoryDTO(SubCategory subCategory) {
		this(subCategory.getId(), subCategory.getName(), subCategory.getCategory());
	}
}
