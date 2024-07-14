package br.com.danielschiavo.pedido.controller;

import br.com.danielschiavo.pedido.dto.response.pedido.MostrarPedidoResponse;
import br.com.danielschiavo.pedido.mapper.PedidoMapper;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.service.pedido.PedidoService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/pedidos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedido - Admin", description = "Todos endpoints relacionados com os pedidos dos clientes para uso exclusivo dos administradores")
public class PedidoAdminController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private PedidoMapper mapper;
	
	@GetMapping("/{clienteId}")
	@Operation(summary = "Pega todos pedidos do customer com id fornecido no parametro da requisição")
	public ResponseEntity<?> pegarTodosPedidosPorClienteId(@PathVariable Long clienteId, Pageable pageable) {
		Page<Pedido> pagePedidos = pedidoService.pegarTodosPedidosPorClienteId(pageable, clienteId);

		List<MostrarPedidoResponse> listaMostrarPedido = pagePedidos.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
		var resposta = new PageImpl<>(listaMostrarPedido, pagePedidos.getPageable(), pagePedidos.getTotalElements());

		return ResponseEntity.ok(Response.success("Sucesso ao pegar todos os pedidos do customer", resposta));
	}

	@GetMapping("/{pedidoId}/clientes/{clienteId}")
	@Operation(summary = "Pega um pedido por id, para obter todos os detalhes sobre ele")
	public ResponseEntity<?> pegarPedidoPorId(@PathVariable UUID pedidoId, @PathVariable Long clienteId) {
		Pedido pedido = pedidoService.pegarPedidoPorId(pedidoId, clienteId);

		return ResponseEntity.ok(Response.success("Sucesso ao pegar pedido do customer", mapper.toDto(pedido)));
	}
}
