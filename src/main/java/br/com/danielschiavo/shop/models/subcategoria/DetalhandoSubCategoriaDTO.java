package br.com.danielschiavo.shop.models.subcategoria;

public record DetalhandoSubCategoriaDTO(
			Long id,
			String nomeSubCategoria,
			String nomeCategoria
		) {

	public DetalhandoSubCategoriaDTO(SubCategoria subCategoria) {
		this(subCategoria.getId(), subCategoria.getNome(), subCategoria.getCategoria().getNome());
	}

}
