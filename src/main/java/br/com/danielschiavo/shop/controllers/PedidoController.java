package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.pedido.MostrarPedidosAConfirmarDTO;
import br.com.danielschiavo.shop.services.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
//	@PostMapping
//	public void createOrder(@RequestBody @Valid AddToCartDTO orderDTO) {
//		Client client = clientService.verifyID(clientId);
//		
//		Address address = addressService.verifyID(clientId, deliveryAddressId);
//		
//		orderService.createOrder(client, address, orderDTO);
//		
//		
//	}
	
	@GetMapping
	public ResponseEntity<Page<MostrarPedidosAConfirmarDTO>> pegarPedidosAConfirmar(Pageable pageable) {
		Page<MostrarPedidosAConfirmarDTO> pagePedidosAConfirmar = pedidoService.pegarPedidosAConfirmar(pageable);
		return ResponseEntity.ok(pagePedidosAConfirmar);
	}

}
