package br.com.danielschiavo.filestorage.service;

import java.io.IOException;
import java.util.UUID;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.filestorage.repository.FileStoragePerfilRepository;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileStoragePerfilService {
	
	@Autowired
	private FileStoragePerfilRepository repository;
	
	public void deletarFotoPerfilNoDisco(String nome) {
		repository.deletar(nome);
	}
	
	public ArquivoInfoDTO pegarFotoPerfilPorNome(String nomeImagem) {
		return repository.pegarFotoPerfilPorNome(nomeImagem);
	}
	
	public String persistirFotoPerfil(MultipartFile arquivo) {
		try {
			String nomeGerado = verificarEGerarNome(arquivo);
			repository.salvar(nomeGerado, arquivo.getBytes());
			return nomeGerado;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}


	public String alterarFotoPerfil(MultipartFile novaFoto, String nomeFotoPerfilAntiga) {
        try {
			repository.deletar(nomeFotoPerfilAntiga);
			String nomeGerado = verificarEGerarNome(novaFoto);
			repository.salvar(nomeGerado, novaFoto.getBytes());
			return nomeGerado;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public String verificarEGerarNome(MultipartFile arquivo) {
		String[] contentType = arquivo.getContentType().split("/");
		String extensao = contentType[1];
		if (!extensao.contains("jpg") && !extensao.contains("jpeg") && !extensao.contains("png"))
			throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png");

		return UUID.randomUUID() + "." + extensao;
	}
}
