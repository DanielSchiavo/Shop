package br.com.danielschiavo.filestorage.controller;

import java.util.List;

import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Produto - Serviço de Armazenamento de Arquivos", description = "Para fazer upload de imagens e videos do produto. Uso exclusivo do backend.")
public class FileStorageProdutoController {

	@Autowired
	private FileStorageProdutoService fileStorageService;

	@DeleteMapping("/admin/produtos/{nomesArquivos}")
	@Operation(summary = "Deleta o arquivo com o nome enviado no parametro da requisição")
	public ResponseEntity<?> deletarImagensProduto(@PathVariable(name = "nomesArquivos") String nomeArquivo) {
		fileStorageService.deletarImagens(nomeArquivo);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(nomeArquivo, "Imagem deletada com sucesso", null);
		return ResponseEntity.ok().body(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}

	@GetMapping("/publico/produto/{nomesArquivos}")
	@Operation(summary = "Recupera os bytes do nome de todas imagens enviadas no parâmetro da requisição")
	public ResponseEntity<?> pegarImagemProduto(
			@PathVariable(name = "nomesArquivos") String nomeImagem) {
		File file = fileStorageService.pegarImagem(nomeImagem);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao recuperar imagem", file.getContent());
		return ResponseEntity.ok(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}

	@PostMapping(path = "/admin/produto", consumes = "multipart/form-data")
	@ResponseBody
	@Operation(summary = "Salva um array de arquivos enviados através de um formulário html e gera os seus respectivos nomes")
	public ResponseEntity<?> persistirImagensProduto(
			@RequestPart(name = "arquivos", required = true) MultipartFile[] arquivos,
			UriComponentsBuilder uriBuilder) {
		List<File> files = fileStorageService.persistirImagens(arquivos);

		List<FileInfoResponse> fileInfoResposeList = files.stream().map(file -> FileInfoResponse.success(file.getFileName(), "Sucesso ao persistir imagem", null)).toList();

		return ResponseEntity.created(uriBuilder.build().toUri()).body(Response.success("Requisição realizada com sucesso", fileInfoResposeList));
	}

//	@DeleteMapping("/admin/produto/{nomesArquivos}")
//	@Operation(summary = "Deleta o arquivo com o nome enviado no parametro da requisição")
//	public ResponseEntity<?> deletarImagensProduto(@PathVariable(name = "nomesArquivos") List<String> nomesArquivos) {
//		fileStorageService.deletarImagens(nomesArquivos);
//
//		List<FileInfoResponse> fileInfoResposeList = nomesArquivos.stream().map(nome -> FileInfoResponse.success(nome, "Imagem deletada com sucesso", null)).toList();

//		return ResponseEntity.ok().body(Response.success("Requisição realizada com sucesso", fileInfoResposeList));

//	}
//	@GetMapping("/publico/produtos/{nomesArquivos}")
//	@Operation(summary = "Recupera os bytes do nome de todas imagens enviadas no parâmetro da requisição")
//	public ResponseEntity<?> pegarImagensProduto(
//			@PathVariable(name = "nomesArquivos") String... nomesImagens) {
//		List<File> files = fileStorageService.pegarImagens(List.of(nomesImagens));
//
//		List<FileInfoResponse> fileInfoResposeList = files.stream().map(file -> {
//			if (file.getContent() == null) {
//				return FileInfoResponse.error(file.getFileName(), "Não foi possivel recuperar a imagem", null);
//			} else {
//				return FileInfoResponse.success(file.getFileName(), "Sucesso ao recuperar imagem", file.getContent());
//			}
//		}).toList();
//		return ResponseEntity.ok(Response.success("Requisição realizada com sucesso", fileInfoResposeList));

//	}

}
