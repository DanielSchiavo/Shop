package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedido", description = "Todos endpoints relacionados com os pedidos do usuário")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping("/pedido")
	@Operation(summary = "Cria um pedido, tanto a partir do carrinho do cliente, quando do botão comprar agora")
	public ResponseEntity<?> criarPedidoBotaoComprarAgoraEComprarDoCarrinho(@RequestBody @Valid CriarPedidoDTO pedidoDTO) {
		MostrarPedidoDTO mostrarPedidoDTO = pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinho(pedidoDTO);
		
		return ResponseEntity.ok(mostrarPedidoDTO);
	}	
	
	@GetMapping("/cliente/pedido")
	@Operation(summary = "Pega todos os pedidos do cliente")
	public ResponseEntity<Page<MostrarPedidoDTO>> pegarPedidosCliente(Pageable pageable) {
		Page<MostrarPedidoDTO> pagePedidosAConfirmar = pedidoService.pegarPedidosCliente(pageable);
		return ResponseEntity.ok(pagePedidosAConfirmar);
	}
	
	@GetMapping("/admin/pedido/{idCliente}")
	@Operation(summary = "Pega todos pedidos do cliente com id fornecido no parametro da requisição")
	public ResponseEntity<Page<MostrarPedidoDTO>> pegarPedidosClientePorId(@PathVariable Long idCliente, Pageable pageable) {
		Page<MostrarPedidoDTO> pagePedidosAConfirmar = pedidoService.pegarPedidosClientePorId(idCliente, pageable);
		return ResponseEntity.ok(pagePedidosAConfirmar);
	}

}
