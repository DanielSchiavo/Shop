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
import java.nio.file.StandardOpenOption;
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
	
	
//	------------------------------
//	------------------------------
//			PRODUTO
//	------------------------------
//	------------------------------
	
	public void deletarArquivoProdutoNoDisco(String nome) {
		try {
			Files.delete(this.raizProduto.resolve(nome));
		} catch (IOException e) {
			throw new FileStorageException("Falha ao excluir arquivo de nome " + nome + " no disco. ", e);
		}
	}
	
	public List<ArquivoInfoDTO> mostrarArquivoProdutoPorListaDeNomes(List<String> listNomes) {
		List<ArquivoInfoDTO> listaArquivosInfoDTO = new ArrayList<>();
		listNomes.forEach(nome -> {
			try {
				byte[] bytes = recuperarBytesArquivoProdutoDoDisco(nome);
				listaArquivosInfoDTO.add(new ArquivoInfoDTO(nome, bytes));
			} catch (FileStorageException e) {
				listaArquivosInfoDTO.add(ArquivoInfoDTO.comErro(nome, e.getMessage()));
			}
		});
		return listaArquivosInfoDTO;
	}
	
	public ArquivoInfoDTO pegarArquivoProdutoPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesArquivoProdutoDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	public List<ArquivoInfoDTO> persistirArrayArquivoProduto(MultipartFile[] arquivos, UriComponentsBuilder uriBuilderBase) {
	    List<ArquivoInfoDTO> arquivosInfo = new ArrayList<>();

	    for (MultipartFile arquivo : arquivos) {
	    	try {
	    		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriBuilderBase.toUriString());
	    		String nomeArquivo = gerarNomeArquivoProduto(arquivo);
	    		byte[] bytesArquivo = salvarNoDiscoArquivoProduto(nomeArquivo, arquivo);
	    		URI uri = uriBuilder.path("/arquivo-produto/" + nomeArquivo).build().toUri();
	    		var arquivoInfo = ArquivoInfoDTO.comUriENomeAntigoArquivo(nomeArquivo, arquivo.getOriginalFilename(), uri.toString(), bytesArquivo);
	    		arquivosInfo.add(arquivoInfo);
			} catch (FileStorageException e) {
				arquivosInfo.add(ArquivoInfoDTO.comErro(arquivo.getOriginalFilename(), e.getMessage()));
			}
	    }
	    
	    return arquivosInfo;
	}
	
	public List<ArquivoInfoDTO> alterarArrayArquivoProduto(MultipartFile[] arquivos, String nomesArquivosASeremExcluidos, UriComponentsBuilder uriBuilderBase) {
		if (arquivos.length == 0 || nomesArquivosASeremExcluidos.isEmpty()) {
			throw new ValidacaoException("Você tem que mandar pelo menos um arquivo e um nomeArquivoASerExcluido");
		}
		List<ArquivoInfoDTO> arquivosInfo = new ArrayList<>();
		String[] split = nomesArquivosASeremExcluidos.trim().split(",");
		
		for(String nomeArquivoASerExcluido : split) {
			try {
				deletarArquivoProdutoNoDisco(nomeArquivoASerExcluido);
				
			} catch (FileStorageException e) {
				arquivosInfo.add(ArquivoInfoDTO.comErro(nomeArquivoASerExcluido, e.getMessage()));
			}
		}
		
		for (MultipartFile arquivo : arquivos) {
			try {
				UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriBuilderBase.toUriString());
				String novoNomeGerado = gerarNomeArquivoProduto(arquivo);
				byte[] bytes = salvarNoDiscoArquivoProduto(novoNomeGerado, arquivo);
				URI uri = uriBuilder.path("/arquivo-produto/" + novoNomeGerado).build().toUri();
				
				arquivosInfo.add(ArquivoInfoDTO.comUri(novoNomeGerado, uri.toString(), bytes));
			} catch (FileStorageException e) {
				arquivosInfo.add(ArquivoInfoDTO.comErro(arquivo.getOriginalFilename(), e.getMessage()));
			}
		}
		return arquivosInfo;
	}

	
//
// METODOS UTILITARIOS DE PRODUTO
//	

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
		if (!contentType[0].contains("image") && !contentType[0].contains("video")) {
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
    
    public byte[] recuperarBytesArquivoProdutoDoDisco(String nomeArquivoProduto) {
		FileUrlResource fileUrlResource;
		try {
			fileUrlResource = new FileUrlResource(raizProduto + "/" + nomeArquivoProduto);
			return fileUrlResource.getContentAsByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel recuperar os bytes do arquivo nome " + nomeArquivoProduto + ", motivo: " + e);
		}
	}

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
	
//	------------------------------
//	------------------------------
//			PERFIL
//	------------------------------
//	------------------------------
	
	public void deletarFotoPerfilNoDisco(String nome) throws IOException {
		if (nome == "Padrao.jpeg") {
			throw new ValidacaoException("Você não pode excluir a foto de perfil Padrao.jpeg");
		}
		Files.delete(this.raizPerfil.resolve(nome));
			
	}
	
	public ArquivoInfoDTO pegarFotoPerfilPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesFotoPerfilDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	public ArquivoInfoDTO persistirFotoPerfil(MultipartFile arquivo, UriComponentsBuilder uriBuilderBase) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriBuilderBase.toUriString());
			String nome = gerarNovoNomeFotoPerfil(arquivo);
			byte[] bytesArquivo = salvarNoDiscoFotoPerfil(nome, arquivo);
			URI uri = uriBuilder.path("/arquivo-produto/" + nome).build().toUri();
			
			return ArquivoInfoDTO.comUri(nome, uri.toString(), bytesArquivo);
			
		} catch (FileStorageException e) {
			return ArquivoInfoDTO.comErro(arquivo.getOriginalFilename(), e.getMessage());
		}
		
	}
	
	public ArquivoInfoDTO alterarFotoPerfil(MultipartFile novaFoto, String nomeArquivoASerSubstituido) {
		if (novaFoto == null || nomeArquivoASerSubstituido.isEmpty()) {
			throw new ValidacaoException("Você tem que mandar pelo menos um arquivo e um nomeArquivoASerExcluido");
		}
		String[] contentType = novaFoto.getContentType().split("/");
		if (!contentType[0].contains("image")) {
			throw new FileStorageException("Só é aceito imagens e videos");
		}
		if (!contentType[1].contains("jpg") && !contentType[1].contains("jpeg") && !contentType[1].contains("png")) {
			throw new FileStorageException("Os tipos aceitos são jpg, jpeg, png");
		}
		try {
			verificarSeExisteFotoPerfilPorNome(nomeArquivoASerSubstituido);
			byte[] bytes = sobrescreverNoDiscoFotoPerfil(novaFoto, nomeArquivoASerSubstituido);
			
			return new ArquivoInfoDTO(nomeArquivoASerSubstituido, bytes);
			
		} catch (FileStorageException e) {
			return ArquivoInfoDTO.comErro(novaFoto.getOriginalFilename(), e.getMessage());
		}
	}
	
	
//
// METODOS UTILITARIOS DE PERFIL
//	
	
	private byte[] sobrescreverNoDiscoFotoPerfil(MultipartFile novaFoto, String nome) {
		try {
			Files.copy(novaFoto.getInputStream(), this.raizPerfil.resolve(nome), StandardCopyOption.REPLACE_EXISTING);
			return novaFoto.getInputStream().readAllBytes();
		} catch (IOException e) {
			throw new FileStorageException("Não foi possível sobrescrever o arquivo no disco");
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

	public byte[] recuperarBytesFotoPerfilDoDisco(String nomeArquivoProduto) {
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
	
	
//	------------------------------
//	------------------------------
//			PEDIDO
//	------------------------------
//	------------------------------

	public ArquivoInfoDTO pegarImagemPedidoPorNome(String nomeArquivo) {
		byte[] bytes = recuperarBytesImagemPedidoDoDisco(nomeArquivo);
		return new ArquivoInfoDTO(nomeArquivo, bytes);
	}
	
	public String persistirOuRecuperarImagemPedido(String nomePrimeiraImagemProduto, Long idProduto) {
		String arquivoInfoDTO = verificarSeExisteImagemPedidoNoDisco(nomePrimeiraImagemProduto);
		if (arquivoInfoDTO != null) {
			return arquivoInfoDTO;
		}
		else {
			String novoNomeImagemPedidoGerado = gerarNomeImagemPedido(idProduto, nomePrimeiraImagemProduto);
			salvarNoDiscoImagemPedido(novoNomeImagemPedidoGerado, nomePrimeiraImagemProduto);
			return novoNomeImagemPedidoGerado;
		}
	}
	
//
// METODOS UTILITARIOS DE PEDIDO
//	
	
	public byte[] recuperarBytesImagemPedidoDoDisco(String nomeArquivo) {
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
		try {
			ArquivoInfoDTO arquivoInfoDTO = pegarArquivoProdutoPorNome(nomePrimeiraImagemProduto);
			byte[] bytes = arquivoInfoDTO.bytesArquivo();
			Files.write(this.raizPedido.resolve(novoNomeImagemPedidoGerado), bytes, StandardOpenOption.CREATE_NEW);
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileStorageException("Não foi possivel salvar o arquivo " + novoNomeImagemPedidoGerado + " no disco");
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
	
	public String verificarSeExisteImagemPedidoNoDisco(String nomePrimeiraImagemProduto) {
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
