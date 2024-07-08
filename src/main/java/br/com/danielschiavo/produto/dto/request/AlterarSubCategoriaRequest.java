package br.com.danielschiavo.produto.dto.request;

public record AlterarSubCategoriaRequest(
		String nome,
		Long categoriaId
		) {

}
