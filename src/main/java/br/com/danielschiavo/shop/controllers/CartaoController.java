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

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.services.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cartão", description = "Todos endpoints relacionados com os cartões do usuário")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;

	@DeleteMapping("/cliente/cartao/{idCartao}")
	@Operation(summary = "Deleta o cartão que contém o id fornecido")
	public ResponseEntity<?> deletarCartaoPorIdToken(@PathVariable Long idCartao) {
		cartaoService.deletarCartaoPorIdToken(idCartao);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/cliente/cartao")
	@Operation(summary = "Pega todos os cartões do usuário que está logado")
	public ResponseEntity<Page<MostrarCartaoDTO>> pegarCartoesClientePorIdToken(Pageable pageable) {
		Page<MostrarCartaoDTO> detalharCartaoDTO = cartaoService.pegarCartoesClientePorIdToken(pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(detalharCartaoDTO);
	}
	
	@PostMapping("/cliente/cartao")
	@Operation(summary = "Cadastra um novo cartão para o usuário")
	public ResponseEntity<MostrarCartaoDTO> cadastrarNovoCartaoPorIdToken(@RequestBody @Valid CadastrarCartaoDTO cartaoDTO) {
		MostrarCartaoDTO detalharCartaoDTO = cartaoService.cadastrarNovoCartaoPorIdToken(cartaoDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(detalharCartaoDTO);
	}
	
	@PutMapping("/cliente/cartao/{idCartao}")
	@Operation(summary = "Altera o cartão padrão do usuário", description = "Se essa requisição for enviada fornecendo um id de cartão que esteja atribuido cartaoPadrao = false, então esse cartão será definido como cartaoPadrao = true e todos os outros como false. Se o id do cartão fornecido for cartaoPadrao = true, a resposta será um BAD_REQUEST porque a única forma de definir um cartaoPadrao = true é fazendo uma requisição PUT para um cartão que esteja definido como cartaoPadrao = false. Só se pode ter 1 cartão padrão.")
	public ResponseEntity<?> alterarCartaoPadraoPorIdToken(@PathVariable Long idCartao) {
		try {
			cartaoService.alterarCartaoPadraoPorIdToken(idCartao);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (ValidacaoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		}
	}
	
	

}
