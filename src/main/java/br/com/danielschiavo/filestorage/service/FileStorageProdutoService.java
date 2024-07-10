package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.FileStorageUtil;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.repository.FileStorageProdutoRepository;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class FileStorageProdutoService {

	@Autowired
	private FileStorageProdutoRepository repository;

	public void deletarImagens(String nomeImagem) {
		repository.deletarImagemPorNome(nomeImagem);
	}

	public void deletarImagens(List<String> nomesArquivos) {
		repository.deletarTodasImagensPorNome(nomesArquivos);
	}

	public File pegarImagem(String nomeImagen) {
		return repository.pegarImagemPorNome(nomeImagen);
	}

    public List<File> pegarImagens(List<String> nomesImagens) {
		return repository.pegarImagensPorListaDeNomes(nomesImagens);
	}
	
	public List<File> persistirImagens(MultipartFile[] arquivos) {
		List<File> files = new ArrayList<>();
		for (MultipartFile arquivo : arquivos) {
			try {
				String nomeGerado = new FileStorageUtil().verificarExtensao(arquivo).gerarNome();
				files.add(repository.salvar(new File(nomeGerado, arquivo.getBytes())));
			} catch (IOException e) {
				throw new FileStorageException("Não foi possivel persistir a imagem", Map.of(arquivo.getOriginalFilename(), e.getMessage()));
			}
		}

		return files;
	}

	public boolean verificarSeImagensExistem(List<String> imagens) {
		return repository.verificarSeImagensExistem(imagens);
	}

	public boolean verificarSeImagemExiste(String imagem) {
		return repository.verificarSeImagemExiste(imagem);
	}

//
// METODOS UTILITARIOS DE PRODUTO
//	

}
