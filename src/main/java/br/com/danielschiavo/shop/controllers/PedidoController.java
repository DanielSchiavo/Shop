package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.services.PedidoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping("/pedido")
	public ResponseEntity<?> criarPedidoBotaoComprarAgoraEComprarDoCarrinho(@RequestBody @Valid CriarPedidoDTO pedidoDTO) {
		pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinho(pedidoDTO);
		
		return ResponseEntity.ok().build();
	}	
	
	@GetMapping("/pedido")
	public ResponseEntity<Page<MostrarPedidoDTO>> pegarPedidosUsuario(Pageable pageable) {
		Page<MostrarPedidoDTO> pagePedidosAConfirmar = pedidoService.pegarPedidosUsuario(pageable);
		return ResponseEntity.ok(pagePedidosAConfirmar);
	}

}
