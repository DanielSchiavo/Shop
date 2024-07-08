package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.filestorage.repository.FileStorageProdutoRepository;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class FileStorageProdutoService {

	@Autowired
	private FileStorageProdutoRepository repository;

	public void deletarImagens(List<String> nomesArquivos) {
		repository.deletarImagensPorNome(nomesArquivos);
	}

	public ArquivoInfoDTO pegarImagem(String nomeImagen) {
		return repository.pegarImagemPorNome(nomeImagen);
	}

    public List<ArquivoInfoDTO> pegarImagens(List<String> nomesImagens) {
		return repository.pegarImagensPorNome(nomesImagens);
	}
	
	public Object persistirImagens(MultipartFile[] arquivos, UriComponentsBuilder uriBuilderBase) {
	    List<ArquivoInfoDTO> arquivosInfo = new ArrayList<>();
	    for (MultipartFile imagem : arquivos) {
			String[] contentType = imagem.getContentType().split("/");
			String extensao = contentType[1];
			if (!extensao.contains("jpg") && !extensao.contains("jpeg") && !extensao.contains("png")) {
				throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png");
			}

	    	try {
	    		String nomeArquivo = UUID.randomUUID().toString() + "." + extensao;

	    		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriBuilderBase.toUriString());
	    		URI uri = uriBuilder.path("/arquivo-produto/" + nomeArquivo).build().toUri();

				var arquivoInfo = ArquivoInfoDTO.comUriENomeAntigoArquivo(nomeArquivo, imagem.getOriginalFilename(), uri.toString());
	    		repository.salvar(arquivoInfo);
				arquivosInfo.add(arquivoInfo);
			} catch (FileStorageException e) {
				arquivosInfo.add(ArquivoInfoDTO.comErro(imagem.getOriginalFilename(), e.getMessage()));
			}
        }
	    
	    return arquivosInfo;
	}

//
// METODOS UTILITARIOS DE PRODUTO
//	

}
