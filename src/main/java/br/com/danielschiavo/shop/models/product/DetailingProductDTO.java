package br.com.danielschiavo.shop.models.product;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.subcategory.SubCategory;

public record DetailingProductDTO(
		Long id, 
		String name, 
		String description,
		String firstImage,
		String images, 
		BigDecimal price,
		Integer quantity,
		Boolean active,
		SubCategory subCategory
		) {
	
	public DetailingProductDTO(Product product) {
		this(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getFirstImage(),
			product.getImages(),
			product.getPrice(),
			product.getQuantity(),
			product.getActive(),
			product.getSubCategory()
			);
	}

}
