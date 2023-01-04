package br.com.danielschiavo.shop.models.product;

import java.math.BigDecimal;

public record ShowProductsDTO(
		Long id, 
		String name, 
		String firstImage, 
		BigDecimal price
		) {
	
	public ShowProductsDTO(Product product) {
		this(product.getId(), product.getName(), product.getFirstImage(), product.getPrice());
	}

}
