package br.com.danielschiavo.shop.models.filestorage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ArquivoInfoDTO(String nomeArquivo, String uri, byte[] bytesArquivo) {

	public ArquivoInfoDTO(String nomeArquivo, byte[] bytes) {
		this(nomeArquivo, null, bytes);
	}}
