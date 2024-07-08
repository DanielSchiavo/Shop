package br.com.danielschiavo.cliente.controller.user;


import java.util.List;

import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.mapper.CartaoMapper;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.service.CartaoService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/clientes/cartoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - Cartão", description = "Todos endpoints relacionados com os cartões do cliente, que o próprio poderá utilizar")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CartaoMapper mapper;
	
	@DeleteMapping("/{idCartao}")
	@Operation(summary = "Deleta o cartão que contém o id fornecido")
	public ResponseEntity<?> deletarCartaoPorIdToken(@PathVariable Long idCartao) {
		Long clienteId = securityService.getClienteId();
		cartaoService.deletarCartaoPorId(idCartao, clienteId);
		return ResponseEntity.ok(Response.success("Cartão deletado com sucesso!", null));
	}

	@GetMapping
	@Operation(summary = "Pega todos os cartões do usuário que está logado")
	public ResponseEntity<?> pegarCartoesClientePorIdToken() {
		Long clienteId = securityService.getClienteId();
		List<Cartao> listaCartao = cartaoService.pegarTodosCartoesPorClienteId(clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Sucesso ao recuperar todos cartões", listaCartao));
	}
	
	@GetMapping("/{cartaoId}")
	@Operation(summary = "Pega o ID do cartão enviado no parametro da requisição")
	public ResponseEntity<?> pegarCartoesClientePorIdToken(@PathVariable Long cartaoId) {
		Long clienteId = securityService.getClienteId();
		Cartao cartao = cartaoService.pegarCartao(cartaoId, clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Sucesso ao recuperar cartão", mapper.toDto(cartao)));
	}
	
	@PostMapping
	@Operation(summary = "Cadastra um novo cartão para o usuário")
	public ResponseEntity<?> cadastrarNovoCartaoPorIdToken(@RequestBody @Valid CadastrarCartaoRequest request) {
		Long clienteId = securityService.getClienteId();
		Cartao cartao = cartaoService.cadastrarCartao(request, clienteId);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Cartão cadastrado com sucesso!", mapper.toDto(cartao)));
	}
	
	@PutMapping("/{cartaoId}")
	@Operation(summary = "Altera o cartão padrão do usuário", description = "Se essa requisição for enviada fornecendo um id de cartão que esteja atribuido cartaoPadrao = false, então esse cartão será definido como cartaoPadrao = true e todos os outros cartões do cliente como false. Se o cartão fornecido no parâmetro através do id estiver como cartaoPadrao = true, esse cartão será definido como cartaoPadrao = false")
	public ResponseEntity<?> alterarCartaoPadraoPorIdToken(@PathVariable Long cartaoId) {
		Long clienteId = securityService.getClienteId();
		cartaoService.alterarCartao(cartaoId, clienteId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Cartão alterado com sucesso!", null));
	}
	
	

}
