package br.com.danielschiavo.filestorage;

import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.UUID;

public class FileStorageUtil {

	private String extensao;

	public static byte[] codificarParaBase64(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}
	
	public static byte[] decodificarDeBase64(byte[] bytesBase64) {
		return Base64.getDecoder().decode(bytesBase64);
	}


	public FileStorageUtil verificarExtensao(MultipartFile file) {
		String[] contentType = file.getContentType().split("/");
		String extensao = contentType[1];
		if (!extensao.contains("jpg") && !extensao.contains("jpeg") && !extensao.contains("png"))
			throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png");
		this.extensao = extensao;
		return this;
	}

	public String gerarNome() {
		return UUID.randomUUID() + "." + this.extensao;
	}
}
