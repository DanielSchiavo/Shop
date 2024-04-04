package br.com.danielschiavo.shop.models.filestorage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ArquivoInfoDTO(
		String nomeArquivo,
		String nomeAntigoArquivo,
		Byte posicao, 
		String erro, 
		String uri, 
		byte[] bytesArquivo) {

	public ArquivoInfoDTO(String nomeArquivo, byte[] bytes) {
		this(nomeArquivo, null, null, null, null, bytes);
	}
	
    public static ArquivoInfoDTO comErro(String nomeArquivo, String erro) {
        return new ArquivoInfoDTO(nomeArquivo, null, null, erro, null, null);
    }
    
    public static ArquivoInfoDTO comUriENomeAntigoArquivo(String nomeArquivo, String nomeAntigoArquivo, String uri, byte[] bytes) {
        return new ArquivoInfoDTO(nomeArquivo, nomeAntigoArquivo, null, null, uri, bytes);
    }
    
    public static ArquivoInfoDTO comUri(String nomeArquivo, String uri, byte[] bytes) {
        return new ArquivoInfoDTO(nomeArquivo, null, null, null, uri, bytes);
    }
}
