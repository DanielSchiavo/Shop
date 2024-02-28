package br.com.danielschiavo.shop.models.subcategoria;

public record MostrarSubCategoriaDTO(
			Long id,
			String nomeSubCategoria,
			String nomeCategoria
		) {

	public MostrarSubCategoriaDTO(SubCategoria subCategoria) {
		this(subCategoria.getId(), subCategoria.getNome(), subCategoria.getCategoria().getNome());
	}

}
