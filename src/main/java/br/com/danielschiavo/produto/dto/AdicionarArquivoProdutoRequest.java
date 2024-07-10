package br.com.danielschiavo.produto.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AdicionarArquivoProdutoRequest(
		@NotNull
		String nome,
		@NotNull
		Byte posicao) {

}
