package br.com.danielschiavo.shop.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.danielschiavo.shop.infra.exceptions.FileStorageException;
import br.com.danielschiavo.shop.models.produto.ArquivosProduto;

@Service
public class FilesStorageService {

	private final Path raizPerfil = Paths.get("imagens/perfil");

	private final Path raizProduto = Paths.get("imagens/produto");

	public List<String> pegarNomeArquivoProduto(MultipartFile[] multipartArquivos, int[] arrayPosicoes, Long produtoId) {
		List<String> nomes = new ArrayList<>();
		for (int i=0; i<multipartArquivos.length; i++) {
			String[] contentType = multipartArquivos[i].getContentType().split("/");
			if (!Arrays.asList(contentType[0]).contains("image") && !Arrays.asList(contentType[0]).contains("video")) {
				throw new RuntimeException("Só é aceito imagens e videos");
			}
			if (!contentType[1].contains("jpg") && !contentType[1].contains("jpeg") && !contentType[1].contains("png") && !contentType[1].contains("mp4") && !contentType[1].contains("avi")) {
				throw new RuntimeException("Os tipos aceitos são jpg, jpeg, png, mp4 e avi");
			}
			String imageName = "APID" + produtoId + "POS" + arrayPosicoes[i] + "." + contentType[1];
			nomes.add(imageName);
			
		}
		return nomes;
	}

	public void salvarNoDiscoArquivosProduto(MultipartFile[] multipartArquivos, List<String> listaNomesArquivos) {
		try {
			for (int i = 0; i < multipartArquivos.length && i < listaNomesArquivos.size(); i++) {
				Files.copy(multipartArquivos[i].getInputStream(), this.raizProduto.resolve(listaNomesArquivos.get(i)),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw new FileStorageException("Falha ao salvar arquivos no disco. ", e);
		}
	}

	public void salvarNoDiscoArquivoProduto(MultipartFile multipartArquivo, String nomeArquivo) {
		try {
			Files.copy(multipartArquivo.getInputStream(), this.raizProduto.resolve(nomeArquivo),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deletarArquivoProdutoNoDisco(String nome) {
		try {
			Files.delete(this.raizProduto.resolve(nome));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<InputStream> carregarArquivoProduto(List<ArquivosProduto> listaArquivosProduto) {
		List<InputStream> inputStream = new ArrayList<>();
		
		listaArquivosProduto.forEach(arquivo -> {
			try {
				Resource resource = new UrlResource(raizProduto + "/" + arquivo.getNome());
				if (resource.exists() && resource.isReadable()) {
					inputStream.add(resource.getInputStream());
				} else {
					throw new RuntimeException("O produto não tem imagem e isso não deveria estar acontecendo");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return inputStream;
	}
	
	public byte[] pegarBytesArquivoProduto(String nomeArquivoProduto) throws IOException {
		FileUrlResource fileUrlResource = new FileUrlResource(raizProduto + "/" + nomeArquivoProduto);
		return fileUrlResource.getContentAsByteArray();
	}
	
	public String pegarNomeFotoPerfil(Long clienteId, MultipartFile fotoPerfil) {
		String[] split = fotoPerfil.getContentType().split("/");
		if (split[0] != "image") {
			throw new RuntimeException("Só é aceito imagens para foto de perfil!");
		}
		String nome = "FP" + clienteId + "." + split[1];
		return nome;
	}

	public void salvarNoDiscoFotoPerfil(String nomeFotoPerfil, MultipartFile fotoPerfil) {
		try {
			Files.copy(fotoPerfil.getInputStream(), this.raizPerfil.resolve(nomeFotoPerfil), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new FileStorageException("Falha ao salvar imagem no disco. ", e);
		}
	}
	

	public byte[] pegarFotoPerfil(String image) throws IOException {
		try {
			FileUrlResource fileUrlResource = new FileUrlResource(raizPerfil + "/" + image);
			if (fileUrlResource.exists()) {
				return fileUrlResource.getContentAsByteArray();
			}
			else {
				String fotoPerfilPadrao = "fotoPerfilPadrao.png";
				FileUrlResource fotoPerfilPadraoResource = new FileUrlResource(raizPerfil + "/" + fotoPerfilPadrao);
				return fotoPerfilPadraoResource.getContentAsByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deletarFotoPerfilNoDisco(String nomeFotoPerfil) {
		File file = new File(raizPerfil.toString() + "/" + nomeFotoPerfil);
		file.delete();
	}

}
