package br.com.danielschiavo.shop.models.produto.subcategoria;

import br.com.danielschiavo.shop.models.produto.categoria.MostrarCategoriaDTO;

public record MostrarSubCategoriaComCategoriaDTO(
			Long id,
			String nome,
			MostrarCategoriaDTO categoria
		) {
}
