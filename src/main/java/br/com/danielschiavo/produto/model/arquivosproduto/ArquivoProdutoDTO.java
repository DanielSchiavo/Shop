package br.com.danielschiavo.produto.model.arquivosproduto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ArquivoProdutoDTO(
		@NotNull
		String nome,
		@NotNull
		Byte posicao) {

}
