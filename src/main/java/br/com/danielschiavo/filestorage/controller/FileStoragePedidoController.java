package br.com.danielschiavo.filestorage.controller;

import java.net.URI;

import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;
import br.com.danielschiavo.filestorage.dto.request.HandleImagemPedidoRequest;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileStoragePedidoService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedido - Serviço de Armazenamento de Arquivos", description = "Para fazer upload da imagem do pedido. Uso exclusivo do backend.")
public class FileStoragePedidoController {

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;
	
	@GetMapping("/cliente/pedido/{nomeImagemPedido}")
	@Operation(summary = "Recupera os bytes da imagem do pedido dado o nome no parametro da requisição")
	public ResponseEntity<?> pegarImagemPedidoPorNome(@PathVariable String nomeImagemPedido) {
		File file = fileStoragePedidoService.pegarImagemPedidoPorNome(nomeImagemPedido);

		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao recuperar imagem", file.getContent());
		return ResponseEntity.ok(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
	
	@PostMapping("/cliente/pedido")
	@Operation(summary = "Cadastra uma nova imagem do pedido e devolve o nome e os bytes da imagem, ou, se já tiver uma imagem cadastrada devolve o nome e os bytes da imagem já cadastrada")
	public ResponseEntity<?> handleImagemPedido(
			@RequestBody HandleImagemPedidoRequest request,
			UriComponentsBuilder uriBuilder
			) {
		File file = fileStoragePedidoService.handleImagemPedido(request.nomePrimeiraImagemProduto(), request.produtoId());

		URI uri = uriBuilder.path("/filestorage/pedido/" + file).build().toUri();
		FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Sucesso ao persistir imagem do pedido", file.getContent());
		return ResponseEntity.created(uri).body(Response.success("Requisição realizada com sucesso", fileInfoResponse));
	}
	
}
