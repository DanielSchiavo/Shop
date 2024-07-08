package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;

import java.util.List;


public record AlterarArquivosResponse(
		List<ArquivoInfoDTO> sucesso,
		List<ArquivoInfoDTO> falha
		) {

}
