package br.com.danielschiavo.shop.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.infra.exceptions.FileStorageException;
import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;

@Service
public class FileStorageService {
	
	private final Path raizPerfil = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/perfil");

	private final Path raizProduto = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/produto");
	
	private final Path raizPedido = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/pedido");

// PRODUTO
	public void verificarSeExisteArquivoProdutoPorNome(String nome) {
		try {
			FileUrlResource fileUrlResource = new FileUrlResource(raizProduto + "/" + nome);
			if (!fileUrlResource.exists()) {
				throw new ValidacaoException("Não existe arquivo-produto com o nome " + nome);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public ArquivoInfoDTO pegarArquivoProdutoPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesArquivoProdutoDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	public ArquivoInfoDTO persistirUmArquivoProduto(MultipartFile arquivo) {
		String nomeArquivo = gerarNomeArquivoProduto(arquivo);
		byte[] bytesArquivo = salvarNoDiscoArquivoProduto(nomeArquivo, arquivo);
		
		return new ArquivoInfoDTO(nomeArquivo, bytesArquivo);
	}
	
	public ArquivoInfoDTO alterarArquivoProduto(MultipartFile arquivo, String nomeAntigoDoArquivo) {
		deletarArquivoProdutoNoDisco(nomeAntigoDoArquivo);
		String novoNomeGerado = gerarNomeArquivoProduto(arquivo);
		byte[] bytes = salvarNoDiscoArquivoProduto(novoNomeGerado, arquivo);
		return new ArquivoInfoDTO(novoNomeGerado, bytes);
	}
	
	public void deletarArquivoProdutoNoDisco(String nome) {
		try {
			Files.delete(this.raizProduto.resolve(nome));
		} catch (IOException e) {
			throw new FileStorageException("Falha ao excluir arquivo de nome " + nome + " no disco. ", e);
		}
	}
	
	public List<ArquivoInfoDTO> persistirArrayArquivoProduto(MultipartFile[] arquivos, UriComponentsBuilder uriBuilderBase) {
	    List<ArquivoInfoDTO> arquivosInfo = new ArrayList<>();

	    for (int i = 0; i < arquivos.length; i++) {
	        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriBuilderBase.toUriString());

	        String nomeArquivo = gerarNomeArquivoProduto(arquivos[i]);
	        byte[] bytesArquivo = salvarNoDiscoArquivoProduto(nomeArquivo, arquivos[i]);

	        URI uri = uriBuilder.path("/arquivo-produto/" + nomeArquivo).build().toUri();

	        var arquivoInfo = new ArquivoInfoDTO(nomeArquivo, uri.toString(), bytesArquivo);

	        arquivosInfo.add(arquivoInfo);
	    }

	    return arquivosInfo;
	}
	
	private byte[] salvarNoDiscoArquivoProduto(String nomeArquivo, MultipartFile arquivo) {
		try {
			byte[] bytes = arquivo.getInputStream().readAllBytes();
			Files.copy(arquivo.getInputStream(), this.raizProduto.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);
			return bytes;
		} catch (Exception e) {
			throw new FileStorageException("Falha ao salvar arquivo de nome "+ nomeArquivo + " no disco. ", e);
		}
	}
	
	private String gerarNomeArquivoProduto(MultipartFile arquivo) {
		String[] contentType = arquivo.getContentType().split("/");
		if (!Arrays.asList(contentType[0]).contains("image") && !Arrays.asList(contentType[0]).contains("video")) {
			throw new FileStorageException("Só é aceito imagens e videos");
		}
		if (!contentType[1].contains("jpg") && !contentType[1].contains("jpeg") && !contentType[1].contains("png")
				&& !contentType[1].contains("mp4") && !contentType[1].contains("avi")) {
			throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png, mp4 e avi");
		}
		String stringUnica = gerarStringUnica();
		return stringUnica + "." + contentType[1];
	}
	
	private static String gerarStringUnica() {
        String string = UUID.randomUUID().toString();
        int divisao = string.length() / 3;
        long timestamp = Instant.now().toEpochMilli();
        String substring = string.substring(0, divisao);
        return substring + timestamp;
    }
    
    private byte[] recuperarBytesArquivoProdutoDoDisco(String nomeArquivoProduto) {
		FileUrlResource fileUrlResource;
		try {
			fileUrlResource = new FileUrlResource(raizProduto + "/" + nomeArquivoProduto);
			return fileUrlResource.getContentAsByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel recuperar os bytes do arquivo nome " + nomeArquivoProduto + ", motivo: " + e);
		}
	}

	public List<ArquivoInfoDTO> mostrarArquivoProdutoPorListaDeNomes(List<String> listNomes) {
		List<ArquivoInfoDTO> listaArquivosInfoDTO = new ArrayList<>();
		listNomes.forEach(nome -> {
			byte[] bytes = recuperarBytesArquivoProdutoDoDisco(nome);
			listaArquivosInfoDTO.add(new ArquivoInfoDTO(nome, bytes));
		});
		return listaArquivosInfoDTO;
	}

// PERFIL
	
	public ArquivoInfoDTO persistirFotoPerfil(MultipartFile arquivo) {
		String nome = gerarNovoNomeFotoPerfil(arquivo);
		byte[] bytesArquivo = salvarNoDiscoFotoPerfil(nome, arquivo);
		
		return new ArquivoInfoDTO(nome, bytesArquivo);
	}
	
	public ArquivoInfoDTO alterarFotoPerfil(MultipartFile novaFoto, String nome) {
		verificarSeExisteFotoPerfilPorNome(nome);
		byte[] bytes = sobrescreverNoDiscoFotoPerfil(novaFoto, nome);
		
		return new ArquivoInfoDTO(nome, bytes);
	}
	
	private byte[] sobrescreverNoDiscoFotoPerfil(MultipartFile novaFoto, String nome) {
		try {
			Files.copy(novaFoto.getInputStream(), this.raizPerfil.resolve(nome), StandardCopyOption.REPLACE_EXISTING);
			return novaFoto.getInputStream().readAllBytes();
		} catch (IOException e) {
			throw new FileStorageException("Não foi possível sobrescrever o arquivo no disco");
		}

	}

	public ArquivoInfoDTO pegarFotoPerfilPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesFotoPerfilDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	public void deletarFotoPerfilNoDisco(String nome) {
		try {
			Files.delete(this.raizPerfil.resolve(nome));
		} catch (IOException e) {
			throw new FileStorageException("Falha ao excluir arquivo de nome " + nome + " no disco. ", e);
		}
	}
	
	private String gerarNovoNomeFotoPerfil(MultipartFile fotoPerfil) {
		String[] contentType = fotoPerfil.getContentType().split("/");
		if (!Arrays.asList(contentType[0]).contains("image")) {
			throw new FileStorageException("Só é aceito imagem na foto de perfil");
		}
		if (!contentType[1].contains("jpg") && !contentType[1].contains("jpeg") && !contentType[1].contains("png")) {
			throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png");
		}
		String stringUnica = gerarStringUnica();
		return stringUnica + "." + contentType[1];
	}

	private byte[] salvarNoDiscoFotoPerfil(String nomeFotoPerfil, MultipartFile fotoPerfil) {
		try {
			byte[] bytes = fotoPerfil.getInputStream().readAllBytes();
			Files.copy(fotoPerfil.getInputStream(), this.raizPerfil.resolve(nomeFotoPerfil), StandardCopyOption.REPLACE_EXISTING);
			return bytes;
		} catch (Exception e) {
			throw new FileStorageException("Falha ao salvar imagem no disco. ", e);
		}
	}

	private byte[] recuperarBytesFotoPerfilDoDisco(String nomeArquivoProduto) {
		FileUrlResource fileUrlResource;
		try {
			fileUrlResource = new FileUrlResource(raizPerfil + "/" + nomeArquivoProduto);
			return fileUrlResource.getContentAsByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel recuperar os bytes da imagem nome " + nomeArquivoProduto + ", motivo: " + e);
		}
	}
	
	public void verificarSeExisteFotoPerfilPorNome(String nome) {
		try {
			FileUrlResource fileUrlResource = new FileUrlResource(raizPerfil + "/" + nome);
			if (!fileUrlResource.exists()) {
				throw new ValidacaoException("Não existe foto de perfil com o nome " + nome);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
//PEDIDO

	public String persistirOuRecuperarImagemPedido(String nomePrimeiraImagemProduto, Long idProduto) {
		String arquivoInfoDTO = verificarSeExisteImagemPedidoNoDisco(nomePrimeiraImagemProduto);
		if (arquivoInfoDTO != null) {
			return arquivoInfoDTO;
		}
		else {
			System.out.println(" AQUI ");
			String novoNomeImagemPedidoGerado = gerarNomeImagemPedido(idProduto, nomePrimeiraImagemProduto);
			salvarNoDiscoImagemPedido(novoNomeImagemPedidoGerado, nomePrimeiraImagemProduto);
			return novoNomeImagemPedidoGerado;
			
		}
	}
	
	public ArquivoInfoDTO pegarImagemPedidoPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesImagemPedidoDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	private byte[] recuperarBytesImagemPedidoDoDisco(String nomeArquivo) {
		FileUrlResource fileUrlResource;
		try {
			fileUrlResource = new FileUrlResource(raizPedido + "/" + nomeArquivo);
			return fileUrlResource.getContentAsByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel recuperar os bytes da imagem nome " + nomeArquivo + ", motivo: " + e);
		}
	}

	private byte[] salvarNoDiscoImagemPedido(String novoNomeImagemPedidoGerado, String nomePrimeiraImagemProduto) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.raizProduto, nomePrimeiraImagemProduto)) {
        	Path fileUri = null;
        	for (Path entry : stream) {
                System.out.println("Arquivo encontrado: " + entry.getFileName());
                fileUri = entry;
                break;
            }
        	FileUrlResource file = new FileUrlResource(fileUri.toString());
        	Files.copy(file.getInputStream(), this.raizPedido.resolve(novoNomeImagemPedidoGerado), StandardCopyOption.REPLACE_EXISTING);
        	return file.getContentAsByteArray();
        } catch (IOException e) {
        	e.printStackTrace();
        	throw new FileStorageException("",e);
        }
	}

	private String gerarNomeImagemPedido(Long idProduto, String nomePrimeiraImagemProduto) {
		String[] split = nomePrimeiraImagemProduto.split("\\.");
		if (!Arrays.asList(split[1]).contains("png") && !Arrays.asList(split[1]).contains("jpg") && !Arrays.asList(split[1]).contains("jpeg")) {
			throw new FileStorageException("Só é aceito imagem na foto de perfil");
		}
		String nome = "PRODID" + idProduto + "-" + nomePrimeiraImagemProduto;
		return nome;
	}
	
	private String verificarSeExisteImagemPedidoNoDisco(String nomePrimeiraImagemProduto) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*" + nomePrimeiraImagemProduto);
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.raizPedido)) {
	        for (Path entry : stream) {
	        	if (matcher.matches(entry.getFileName())) {
		        	return entry.getFileName().toString(); 
	            }
	        }
	        return null;
	    } catch (IOException e) {
	        throw new FileStorageException("Falha ao tentar recuperar imagem do pedido no disco.", e);
	    }
	}
}
