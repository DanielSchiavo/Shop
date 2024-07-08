package br.com.danielschiavo.produto.model.categoria.subcategoria;

public record AlterarSubCategoriaRequest(
		String nome,
		Long categoriaId
		) {

}
