package br.com.danielschiavo.shop.models.categoria;

import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;

public record MostrarCategoriaComSubCategoriaDTO(
			Long id,
			String nome,
			MostrarSubCategoriaDTO subCategoria
		) {

	public MostrarCategoriaComSubCategoriaDTO(SubCategoria subCategoria, Categoria categoria) {
		this(categoria.getId(), categoria.getNome(), new MostrarSubCategoriaDTO(subCategoria));
	}

}
