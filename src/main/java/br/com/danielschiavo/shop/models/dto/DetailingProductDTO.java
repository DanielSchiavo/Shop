package br.com.danielschiavo.shop.models.dto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.Product;
import br.com.danielschiavo.shop.models.SubCategory;

public record DetailingProductDTO(
		Long id, 
		String name, 
		String description,
		String images, 
		BigDecimal price, 
		Integer quantity,  
		Boolean active, 
		SubCategory subCategory) {
	
	public DetailingProductDTO(Product product) {
		this(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getImages(),
			product.getPrice()
			product.getQuantity(),
			product.getActive(),
			product.getSubCategory());
	}

}
