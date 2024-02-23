package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.infra.security.DadosAutenticacaoDTO;
import br.com.danielschiavo.shop.infra.security.TokenDTO;
import br.com.danielschiavo.shop.services.AutenticacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class AutenticacaoController {
	

	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid DadosAutenticacaoDTO dadosAutenticacao) {
		TokenDTO tokenDTO = autenticacaoService.login(dadosAutenticacao);
		
		return ResponseEntity.ok(tokenDTO);
		
	}

}
