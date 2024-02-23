package br.com.danielschiavo.shop.models.subcategoria;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubCategoriaDTO(
		@NotBlank
		String nome,
		@NotNull
		@NumberFormat
		Long categoria_id
		) {

}
