package br.com.danielschiavo.produto.dto.response;

public record MostrarSubCategoriaComCategoriaResponse(
			Long id,
			String nome,
			MostrarCategoriaResponse categoria
		) {
}
