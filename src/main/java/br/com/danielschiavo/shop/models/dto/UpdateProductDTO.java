package br.com.danielschiavo.shop.models.dto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.SubCategory;
import jakarta.validation.constraints.NotNull;

public record UpdateProductDTO(
		@NotNull
		Long id,
		String name,
		String description,
		String images,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		SubCategory subCategory
		) {

}
