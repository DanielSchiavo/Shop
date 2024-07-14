package br.com.danielschiavo.filestorage.service;

import java.io.IOException;

import br.com.danielschiavo.filestorage.FileStorageUtil;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.repository.FileStoragePerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileStoragePerfilService {
	
	@Autowired
	private FileStoragePerfilRepository repository;
	
	public void deleteProfilePictureInDisk(String nome) {
		repository.deletarPorNome(nome);
	}
	
	public File pegarFotoPerfilPorNome(String nomeImagem) {
		return repository.pegarFotoPerfilPorNome(nomeImagem);
	}
	
	public File persistirFotoPerfil(MultipartFile arquivo) {
		try {
			String nomeGerado = new FileStorageUtil().verificarExtensao(arquivo).gerarNome();

			return repository.salvar(nomeGerado, arquivo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}


	public File updateProfilePicture(MultipartFile novaFoto, String nomeFotoPerfilAntiga) {
		repository.deletarPorNome(nomeFotoPerfilAntiga);

		return persistirFotoPerfil(novaFoto);
    }

}
