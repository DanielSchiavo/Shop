package br.com.danielschiavo.shop.models.subcategoria;

import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;

public record MostrarSubCategoriaComCategoriaDTO(
			Long id,
			String nome,
			MostrarCategoriaDTO categoria
		) {

	public MostrarSubCategoriaComCategoriaDTO(SubCategoria subCategoria) {
		this(subCategoria.getId(), subCategoria.getNome(), new MostrarCategoriaDTO(subCategoria.getCategoria()));
	}

	public static MostrarSubCategoriaComCategoriaDTO converterSubCategoriaParaMostrarSubCategoriaComCategoriaDTO(SubCategoria subCategoria) {
		return new MostrarSubCategoriaComCategoriaDTO(subCategoria);
	}
}
