package br.com.danielschiavo.cliente.controller.user;


import br.com.danielschiavo.cliente.dto.request.cliente.AlterarClienteRequest;
import br.com.danielschiavo.cliente.dto.request.cliente.CadastrarClienteRequest;
import br.com.danielschiavo.cliente.mapper.ClienteMapper;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.cliente.ClienteService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/clientes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - User", description = "Todos endpoints relacionados com o cliente, que o próprio poderá utilizar")
public class ClienteUserController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ClienteMapper mapper;
	
	@GetMapping("/pagina-inicial")
	public ResponseEntity<?> pegarClientePaginaInicial() {
		Long clienteId = securityService.getClienteId();
		Cliente cliente = clienteService.pegarClientePorIdPaginaInicial(clienteId);
		
		return ResponseEntity.ok(mapper.toPaginaInicialDto(cliente));
	}

	@GetMapping
	@Operation(summary = "Mostra todos os dados do cliente")
	public ResponseEntity<?> pegarCliente() {
		Long clienteId = securityService.getClienteId();
		Cliente cliente = clienteService.pegarClientePorId(clienteId);

		return ResponseEntity.ok(Response.success("Sucesso ao recuperar dados do cliente", mapper.toDto(cliente)));
	}
	
	@PostMapping("/register")
	@Operation(summary = "Cadastro de cliente")
	public ResponseEntity<?> cadastrarCliente(@RequestBody @Valid CadastrarClienteRequest request) {
		Cliente cadastrarCliente = mapper.toEntity(request);
		Cliente cliente = clienteService.cadastrarCliente(cadastrarCliente);

		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Cadastrado com sucesso!", null));

	}
	
	@PutMapping
	@Operation(summary = "Cliente altera seus próprios dados")
	public ResponseEntity<?> alterarCliente(@RequestBody @Valid AlterarClienteRequest request) {
		Long clienteId = securityService.getClienteId();
		Cliente clienteAtualizado = mapper.toEntity(request);

		Cliente cliente = clienteService.alterarClientePorId(clienteId, clienteAtualizado);
		return ResponseEntity.ok(Response.success("Dados alterados com sucesso!", null));
	}
	
	@PutMapping("/foto-perfil")
	@Operation(summary = "Alterar a foto do perfil do cliente")
	public ResponseEntity<?> alterarFotoPerfil(@RequestPart(name = "foto", required = true) MultipartFile novaFoto) {
		Long clienteId = securityService.getClienteId();
		Cliente cliente = clienteService.alterarFotoPerfilPorId(novaFoto, clienteId);
		return ResponseEntity.ok(Response.success("Foto alterada com sucesso!", null));
	}

	@DeleteMapping("/foto-perfil")
	@Operation(summary = "Deleta foto do perfil do cliente")
	public ResponseEntity<?> deletarFotoPerfil() {
		Long clienteId = securityService.getClienteId();
		clienteService.deletarFotoPerfilPorId(clienteId);
		return ResponseEntity.ok().body(Response.success("Foto de perfil deletada com sucesso!", null));
	}
}
