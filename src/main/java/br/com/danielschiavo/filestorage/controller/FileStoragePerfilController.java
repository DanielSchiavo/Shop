package br.com.danielschiavo.filestorage.controller;


import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileStoragePerfilService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Serviço de Armazenamento de Arquivos", description = "Para fazer upload da foto de perfil do customer. Uso exclusivo do backend.")
public class FileStoragePerfilController {

	@Autowired
	private FileStoragePerfilService fileStoragePerfilService;
	
	@DeleteMapping("/cliente/perfil/{nomeFotoPerfil}")
	@Operation(summary = "Deleta a foto de perfil com o name enviado no parametro da requisição")
	public ResponseEntity<?> deletarFotoPerfil(@PathVariable String nomeFotoPerfil) {
		fileStoragePerfilService.deleteProfilePictureInDisk(nomeFotoPerfil);
		FileInfoResponse fileInfoResponse = FileInfoResponse.success(nomeFotoPerfil, "Foto de perfil deletada com sucesso!", null);
		return ResponseEntity.ok().body(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
	
	@GetMapping("/cliente/perfil/{nomeFotoPerfil}")
	@Operation(summary = "Pega uma foto de perfil dado o name da foto no parametro da requisição")
	public ResponseEntity<?> pegarFotoPerfilPorNome(@PathVariable String nomeFotoPerfil) {
		File file = fileStoragePerfilService.pegarFotoPerfilPorNome(nomeFotoPerfil);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao recuperar foto de perfil", file.getContent());
		return ResponseEntity.ok(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
	
	@PostMapping("/cliente/perfil/")
	@Operation(summary = "Cadastra uma foto de perfil enviada através de um formulario html e gera um name")
	public ResponseEntity<?> cadastrarFotoPerfil(
			@RequestPart(name = "foto", required = true) MultipartFile foto) {
		File file = fileStoragePerfilService.persistirFotoPerfil(foto);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao persistir foto de perfil", null);
		return ResponseEntity.ok().body(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
	
	@PutMapping("/cliente/{nomeFotoPerfilAntiga}")
	@Operation(summary = "Deleta o nomeAntigoDoArquivo e salva o arquivo enviado e gera um novo name")
	public ResponseEntity<?> alterarFotoPerfil(
			@RequestPart(name = "foto", required = true) MultipartFile novaFoto,
			@RequestParam String nomeFotoPerfilAntiga,
			UriComponentsBuilder uriBuilder
			) {
		File file = fileStoragePerfilService.updateProfilePicture(novaFoto, nomeFotoPerfilAntiga);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao alterar foto de perfil", null);

		return ResponseEntity.ok(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
}
