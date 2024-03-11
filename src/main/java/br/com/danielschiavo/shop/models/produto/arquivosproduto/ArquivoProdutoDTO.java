package br.com.danielschiavo.shop.models.produto.arquivosproduto;

import jakarta.validation.constraints.NotNull;

public record ArquivoProdutoDTO(
		@NotNull
		String nome,
		@NotNull
		Integer posicao) {

}
