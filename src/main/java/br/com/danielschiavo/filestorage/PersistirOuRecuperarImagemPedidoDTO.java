package br.com.danielschiavo.filestorage;

import lombok.Builder;

@Builder
public record PersistirOuRecuperarImagemPedidoDTO(
		String nomePrimeiraImagemProduto,
		Long idProduto
		) {

}
