package br.com.danielschiavo.shop.models.product;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.subcategory.SubCategory;

public record UpdateProductDTO(
		String name,
		String description,
		String images,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		SubCategory subCategory
		) {

}
