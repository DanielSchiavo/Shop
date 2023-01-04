package br.com.danielschiavo.shop.models.category;

public record DetailingCategoryDTO(Long id, String name) {
	
	public DetailingCategoryDTO(Category category) {
		this(category.getId(), category.getName());
	}

}
