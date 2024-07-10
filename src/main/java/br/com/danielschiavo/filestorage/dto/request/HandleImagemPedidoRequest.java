package br.com.danielschiavo.filestorage.dto.request;

import lombok.Builder;

@Builder
public record HandleImagemPedidoRequest(
		String nomePrimeiraImagemProduto,
		Long produtoId
		) {

}
