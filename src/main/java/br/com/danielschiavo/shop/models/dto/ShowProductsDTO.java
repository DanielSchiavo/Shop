package br.com.danielschiavo.shop.models.dto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.Product;

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
