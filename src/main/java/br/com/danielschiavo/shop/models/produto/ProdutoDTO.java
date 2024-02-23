package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProdutoDTO(
		@JsonProperty("nome")
		String nome,
		@JsonProperty("descricao")
		String descricao,
		@JsonProperty("preco")
		BigDecimal preco,
		@JsonProperty("quantidade")
		Integer quantidade,
		@JsonProperty("ativo")
		Boolean ativo,
		@JsonProperty("subCategoriaId")
		Long subCategoriaId
		) {

}
