package br.com.danielschiavo.shop.models.produto.subcategoria;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarSubCategoriaDTO(
		@NotBlank
		String nome,
		@NotNull
		@NumberFormat
		Long categoria_id
		) {

}
