package br.com.danielschiavo.cliente.controller.user;


import java.util.List;

import br.com.danielschiavo.cliente.dto.request.endereco.AlterarEnderecoRequest;
import br.com.danielschiavo.cliente.dto.request.endereco.CadastrarEnderecoRequest;
import br.com.danielschiavo.cliente.model.entity.Endereco;
import br.com.danielschiavo.cliente.service.endereco.EnderecoService;
import br.com.danielschiavo.cliente.mapper.EnderecoMapper;
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

@RestController
@RequestMapping("/user/clientes/enderecos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - Endereço", description = "Todos endpoints relacionados com os endereços do cliente, que o próprio poderá utilizar")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private EnderecoMapper mapper;
	
	@DeleteMapping("/{enderecoId}")
	@Operation(summary = "Deletar um endereço por id")
	public ResponseEntity<?> deletarEndereco(@PathVariable Long enderecoId) {
		Long clienteId = securityService.getClienteId();
		enderecoService.deletarEnderecoPorId(enderecoId, clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Endereço deletado com sucesso!", null));
	}
	
	@GetMapping
	@Operation(summary = "Pegar todos endereços do cliente")
	public ResponseEntity<?> pegarEnderecosClientePorIdToken() {
		Long clienteId = securityService.getClienteId();
		List<Endereco> enderecos = enderecoService.pegarTodosEnderecosCliente(clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Sucesso ao recuperar todos os endereços", mapper.toDto(enderecos)));
	}
	
	@GetMapping("/{enderecoId}")
	@Operation(summary = "Pegar endereço do cliente por id")
	public ResponseEntity<?> pegarEnderecoPorId(@PathVariable Long enderecoId) {
		Long clienteId = securityService.getClienteId();
		Endereco endereco = enderecoService.pegarEnderecoPorId(enderecoId, clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Sucesso ao recuperar o endereço", mapper.toDto(endereco)));
	}
	
	@PostMapping
	@Operation(summary = "Cadastrar novo endereço para o cliente")
	public ResponseEntity<?> cadastrarNovoEndereco(@RequestBody @Valid CadastrarEnderecoRequest request) {
		Long clienteId = securityService.getClienteId();
		Endereco endereco = enderecoService.cadastrarEndereco(request, clienteId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Endereço cadastrado com sucesso!", null));
	}
	
	@PutMapping("/{enderecoId}")
	@Operation(summary = "Alterar um endereço por id")
	public ResponseEntity<?> alterarEnderecoPorIdToken(@PathVariable Long enderecoId, @RequestBody AlterarEnderecoRequest request) {
		Long clienteId = securityService.getClienteId();
		Endereco endereco = enderecoService.alterarEnderecoPorIdToken(request, clienteId, enderecoId);
		return ResponseEntity.ok().body(Response.success("Endereço alterado com sucesso!", mapper.toDto(endereco)));
	}
	
}
