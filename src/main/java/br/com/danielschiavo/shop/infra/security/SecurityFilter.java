package br.com.danielschiavo.shop.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.danielschiavo.shop.repositories.ClienteRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenJWTService tokenJWTService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
			, FilterChain filterChain) throws ServletException, IOException {
		
		var token = recoverToken(request);
		
		if (token != null) {
			Long subject = tokenJWTService.getClaimIdJWT(token);
			UserDetails client = clienteRepository.buscarPorId(subject);
			var authentication = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		
		if (authorization != null) {
			return authorization.replace("Bearer ", "");
		}
		return null;
	}

}
