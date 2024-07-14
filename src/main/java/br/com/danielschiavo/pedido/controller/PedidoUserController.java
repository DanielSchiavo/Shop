package br.com.danielschiavo.pedido.controller;

import br.com.danielschiavo.pedido.mapper.PedidoMapper;
import br.com.danielschiavo.pedido.dto.request.pedido.FazerPedidoRequest;
import br.com.danielschiavo.pedido.dto.response.pedido.MostrarPedidoResponse;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.service.pedido.PedidoService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/pedidos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedido - User", description = "Todos endpoints relacionados com os pedidos do customer, que o próprio poderá utilizar")
public class PedidoUserController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PedidoMapper mapper;

	@GetMapping("/{pedidoId}")
	@Operation(summary = "Pega um pedido por id, para obter todos os detalhes sobre ele")
	public ResponseEntity<?> pegarPedidoPorId(@PathVariable UUID pedidoId) {
		Long clienteId = securityService.getCustomerId();
		Pedido pedido = pedidoService.pegarPedidoPorId(pedidoId, clienteId);

		return ResponseEntity.ok(Response.success("Sucesso ao pegar todos os pedidos do customer", mapper.toDto(pedido)));
	}
	
	@GetMapping
	@Operation(summary = "Pega todos os pedidos do customer")
	public ResponseEntity<?> pegarTodosPedidos(Pageable pageable) {
		Long clienteId = securityService.getCustomerId();
		Page<Pedido> pagePedidos = pedidoService.pegarTodosPedidosPorClienteId(pageable, clienteId);

		List<MostrarPedidoResponse> listaMostrarPedido = pagePedidos.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
		var resposta = new PageImpl<>(listaMostrarPedido, pagePedidos.getPageable(), pagePedidos.getTotalElements());

		return ResponseEntity.ok(Response.success("Sucesso ao pegar todos os pedidos do customer", resposta));
	}
	
	@PostMapping
	@Operation(summary = "Cria um pedido em name do customer autenticado que está no token")
	public ResponseEntity<?> realizarPedido(@RequestBody @Valid FazerPedidoRequest request, Long clienteId) {
		Pedido fazerPedido = mapper.toEntity(request);
		Pedido pedido = pedidoService.realizarPedido(clienteId, fazerPedido);

		return ResponseEntity.ok(Response.success("Pedido realizado com sucesso!", mapper.toDto(pedido)));
	}
}
