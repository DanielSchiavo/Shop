package br.com.danielschiavo.shop.models.categoria;

public record MostrarCategoriaDTO(
			Long id,
			String nome
		) {

	public MostrarCategoriaDTO(Categoria categoria) {
		this(categoria.getId(), categoria.getNome());
	}

}
