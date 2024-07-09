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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedido - User", description = "Todos endpoints relacionados com os pedidos do cliente, que o próprio poderá utilizar")
public class PedidoUserController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PedidoMapper mapper;
	
	@GetMapping("/cliente/pedido")
	@Operation(summary = "Pega todos os pedidos do cliente")
	public ResponseEntity<?> pegarPedidosClientePorIdToken(Pageable pageable) {
		Long clienteId = securityService.getClienteId();
		Page<Pedido> pagePedidos = pedidoService.pegarPedidosPorClienteId(pageable, clienteId);

		List<MostrarPedidoResponse> listaMostrarPedido = pagePedidos.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
		var resposta = new PageImpl<>(listaMostrarPedido, pagePedidos.getPageable(), pagePedidos.getTotalElements());

		return ResponseEntity.ok(Response.success("Sucesso ao pegar todos os pedidos do cliente", resposta));
	}
	
	@PostMapping("/cliente/pedido")
	@Operation(summary = "Cria um pedido em nome do cliente autenticado que está no token")
	public ResponseEntity<?> realizarPedido(@RequestBody @Valid FazerPedidoRequest request, Long clienteId) {
		Pedido pedido = pedidoService.realizarPedido(request, clienteId);

		return ResponseEntity.ok(Response.success("Pedido realizado com sucesso!", mapper.toDto(pedido)));
	}
}
