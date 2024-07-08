package br.com.danielschiavo.filestorage.repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.filestorage.Base64Utils;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("fileStorageRepository")
public class FileStorageRepository {
	
	protected Object deletar(Path caminho, String... nomesImagens) {
		verificacaoDiretorioAtual();
		
		List<ArquivoInfoDTO> listaArquivosInfoDTO = new ArrayList<>();
		
		for (String nomeImagem : nomesImagens) {
			if (nomeImagem.equals("Padrao.jpeg"))
				throw new FileStorageException("O arquivo não pode ser excluido porque é a imagem padrão para produtos sem fotos.");
			
			try {
				boolean deletou = Files.deleteIfExists(caminho.resolve(nomeImagem));
				if (deletou)
					listaArquivosInfoDTO.add(ArquivoInfoDTO.comNomeEMensagem(nomeImagem, "Imagem deletada com sucesso!"));
				else
					throw new FileStorageException("O arquivo não existe, portanto não foi possivel exclui-lo");
			}
			catch (FileStorageException | IOException e) {
				listaArquivosInfoDTO.add(ArquivoInfoDTO.comErro(nomeImagem, e.getMessage()));
			}
		}
		
		return nomesImagens.length > 1 ? 
				listaArquivosInfoDTO.get(0) : 
					listaArquivosInfoDTO;
	}

	protected void salvar(Path caminho, String nomeImagem, byte[] bytes) {
		verificacaoDiretorioAtual();
		
		try {
			Files.write(caminho.resolve(nomeImagem), bytes, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel salvar o arquivo " + nomeImagem + " no disco");
		}
	}

	protected Object pegar(Path caminho, String... nomesImagens) {
    	verificacaoDiretorioAtual();

    	String nomeAtual = null;
    	List<ArquivoInfoDTO> listaArquivosInfoDTO = new ArrayList<>();

		for (String nome : nomesImagens) {
			try {
				nomeAtual = nome;
				byte[] allBytes = Files.readAllBytes(caminho.resolve(nome));
				byte[] allBytesBase64 = Base64Utils.codificarParaBase64(allBytes);
				listaArquivosInfoDTO.add(new ArquivoInfoDTO(nome, allBytesBase64));
			} catch (IOException e) {
				listaArquivosInfoDTO.add(ArquivoInfoDTO.comErro(nome, "Não foi possivel recuperar os bytes do arquivo nome " + nomeAtual
						+ ", motivo: " + e.getMessage()));
				e.printStackTrace();
			}
		}
		
		return listaArquivosInfoDTO.size() == 1 ? 
				listaArquivosInfoDTO.get(0) : 
					listaArquivosInfoDTO;
	}

	protected Optional<String> verificarSeExisteNoDisco(Path caminho, String nomeImagem, String comecaCom, String terminaCom) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + comecaCom + nomeImagem + terminaCom);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminho)) {
			for (Path entry : stream) {
				if (matcher.matches(entry.getFileName())) {
					return Optional.of(entry.getFileName().toString());
				}
			}
			return Optional.empty();
		} catch (IOException e) {
			throw new FileStorageException("Falha ao tentar recuperar imagem no disco.", e);
		}
	}

	protected void verificacaoDiretorioAtual() {
    	String diretorioAtual = System.getProperty("user.dir");
    	System.out.println(" O diretorio atual é: " + diretorioAtual);
    }

}
