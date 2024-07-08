package br.com.danielschiavo.produto.dto.request;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarSubCategoriaRequest(
		@NotBlank
		String nome,
		@NotNull
		@NumberFormat
		Long categoriaId
		) {

}
