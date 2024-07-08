package br.com.danielschiavo.produto.model.categoria.subcategoria;

import br.com.danielschiavo.produto.model.categoria.MostrarCategoriaResponse;

public record MostrarSubCategoriaComCategoriaResponse(
			Long id,
			String nome,
			MostrarCategoriaResponse categoria
		) {
}
