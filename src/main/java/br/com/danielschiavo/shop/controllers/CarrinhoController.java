package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.carrinho.DeletarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.services.CarrinhoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class CarrinhoController {
	
	@Autowired
	private CarrinhoService carrinhoService;
	
	@PostMapping("/cliente/carrinho")
	public ResponseEntity<Object> adicionarProdutosNoCarrinho(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.adicionarNoCarrinho(itemCarrinhoDTO);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/cliente/carrinho")
	public ResponseEntity<MostrarCarrinhoClienteDTO> pegarCarrinhoCliente() {
		MostrarCarrinhoClienteDTO mostrarCarrinhoClienteDTO = carrinhoService.pegarCarrinhoCliente();
		
		return ResponseEntity.ok(mostrarCarrinhoClienteDTO);
	}
	
	@PutMapping("/cliente/carrinho")
	public ResponseEntity<Object> alterarQuantidadeProdutoNoCarrinho(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.mudarCarrinhoCliente(itemCarrinhoDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/cliente/carrinho")
	public ResponseEntity<Object> deletarProdutoNoCarrinho(@RequestBody @Valid DeletarItemCarrinhoDTO deletarItemNoCarrinhoDTO) {
		carrinhoService.deletarItemCarrinho(deletarItemNoCarrinhoDTO);
		return ResponseEntity.noContent().build();
	}

}
