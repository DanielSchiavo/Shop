package br.com.danielschiavo.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.DadosAutenticacaoDTO;
import br.com.danielschiavo.shop.infra.security.TokenDTO;
import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import jakarta.validation.Valid;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private ClienteRepository clientRepository;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenJWTService tokenService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return clientRepository.findByEmail(email);
	}

	public TokenDTO login(@Valid DadosAutenticacaoDTO dadosAutenticacao) {
		var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dadosAutenticacao.login(), dadosAutenticacao.senha());
		var authentication = manager.authenticate(usernamePasswordAuthenticationToken);
		
		String token = tokenService.generateToken((Cliente) authentication.getPrincipal());
		
		return new TokenDTO(token);
	}

}
