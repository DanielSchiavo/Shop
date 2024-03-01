package br.com.danielschiavo.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import br.com.danielschiavo.shop.models.cartao.CartaoDTO;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.services.CartaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@PostMapping("/cliente/cartao")
	public ResponseEntity<MostrarCartaoDTO> cadastrarNovoCartao(@RequestBody @Valid CartaoDTO cartaoDTO) {
		MostrarCartaoDTO detalharCartaoDTO = cartaoService.cadastrarNovoCartao(cartaoDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(detalharCartaoDTO);
	}
	
	@PutMapping("/cliente/cartao/{id}")
	public ResponseEntity<MostrarCartaoDTO> alterarCartaoPadrao(@PathVariable Long id) {
		cartaoService.alterarCartaoPadrao(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/cliente/cartao")
	public ResponseEntity<Page<MostrarCartaoDTO>> pegarCartoesCliente(Pageable pageable) {
		Page<MostrarCartaoDTO> detalharCartaoDTO = cartaoService.pegarCartoesCliente(pageable);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(detalharCartaoDTO);
	}
	
	@DeleteMapping("/cliente/cartao/{id}")
	public ResponseEntity<?> deletarCartao(@PathVariable Long id) {
		cartaoService.deletarCartao(id);
		
		return ResponseEntity.noContent().build();
	}
	

}
