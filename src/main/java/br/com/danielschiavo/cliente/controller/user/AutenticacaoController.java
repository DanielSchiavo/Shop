package br.com.danielschiavo.cliente.controller.user;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.cliente.dto.request.cliente.DadosAutenticacaoDTO;
import br.com.danielschiavo.shared.infra.security.TokenJWTService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Login")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenJWTService tokenService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid DadosAutenticacaoDTO dadosAutenticacao) {
		try {
			var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dadosAutenticacao.login(), dadosAutenticacao.senha());
			var authentication = manager.authenticate(usernamePasswordAuthenticationToken);
			
			String token = tokenService.generateToken((Cliente) authentication.getPrincipal());
			
			return ResponseEntity.ok().body("{ \"token\": \"" + token + "\" }");
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

}
