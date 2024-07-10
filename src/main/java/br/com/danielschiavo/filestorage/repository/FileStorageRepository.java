package br.com.danielschiavo.filestorage.repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.filestorage.FileStorageUtil;
import br.com.danielschiavo.filestorage.exception.FileNotFoundException;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("fileStorageRepository")
public class FileStorageRepository {
	
	protected void deletar(Path caminho, String nomeImagem) {
		verificacaoDiretorioAtual();

		if (nomeImagem.equals("Padrao.jpeg"))
			throw new FileStorageException("O arquivo não pode ser excluido porque é a imagem padrão para produtos sem fotos.");

		try {
			boolean deletou = Files.deleteIfExists(caminho.resolve(nomeImagem));
			if (!deletou)
				throw new FileNotFoundException("O arquivo não existe, portanto não foi possivel exclui-lo");
		} catch (IOException e) {
			throw new FileStorageException(e.getMessage());
		}
	}

	protected void salvar(Path caminho, String nomeImagem, byte[] bytes) {
		verificacaoDiretorioAtual();
		
		try {
			Files.write(caminho.resolve(nomeImagem), bytes, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			throw new FileStorageException("Não foi possivel salvar o arquivo " + nomeImagem + " no disco");
		}
	}

	protected byte[] pegar(Path caminho, String nomeImagem) {
    	verificacaoDiretorioAtual();

		try {
			boolean exists;
			if (nomeImagem == null) {
				exists = Files.exists(caminho, LinkOption.NOFOLLOW_LINKS);
			} else {
				exists = Files.exists(caminho.resolve(nomeImagem), LinkOption.NOFOLLOW_LINKS);
			}

			if (!exists)
				throw new FileNotFoundException("O arquivo especificado não existe");

			byte[] allBytes = Files.readAllBytes(caminho.resolve(nomeImagem));
			return FileStorageUtil.codificarParaBase64(allBytes);
		} catch (IOException e) {
			throw new FileStorageException(e.getMessage());
		}
	}

	protected Optional<byte[]> verificarSeExisteNoDisco(Path caminho, String nomeImagem, String comecaCom, String terminaCom) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + comecaCom + nomeImagem + terminaCom);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminho)) {
			for (Path entry : stream) {
				if (matcher.matches(entry.getFileName())) {
					return Optional.of(pegar(entry, null));
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

	protected boolean verificarSeImagensExistem(Path path, List<String> nomesImagens) {
		return nomesImagens.stream().allMatch(nome -> verificarSeImagemExiste(path, nome));
	}

	protected boolean verificarSeImagemExiste(Path path, String nomeImagem) {
		return Files.exists(path.resolve(nomeImagem), LinkOption.NOFOLLOW_LINKS);
	}
}
