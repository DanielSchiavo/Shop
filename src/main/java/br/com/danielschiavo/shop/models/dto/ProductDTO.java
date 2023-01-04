package br.com.danielschiavo.shop.models.dto;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import br.com.danielschiavo.shop.models.Product;
import br.com.danielschiavo.shop.models.SubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
		@NotBlank
		String name,
		@NotBlank
		String description,
		@NotBlank
		String firstImage,
		@NotBlank
		String images,
		@NumberFormat
		@NotNull
		BigDecimal price,
		@NumberFormat
		@NotNull
		Integer quantity,
		@NotBlank
		Boolean active,
		@NotNull
		SubCategory subCategory
		) {
	
	public ProductDTO(Product product) {
		this(
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
