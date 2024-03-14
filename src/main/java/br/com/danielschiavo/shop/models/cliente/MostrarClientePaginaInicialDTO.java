package br.com.danielschiavo.shop.models.cliente;

import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;

public record MostrarClientePaginaInicialDTO(
		String nome,
		ArquivoInfoDTO fotoPerfil
		) {

}
