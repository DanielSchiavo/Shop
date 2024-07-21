package br.com.danielschiavo.shared.infra.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenJWTService tokenJWTService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
			, FilterChain filterChain) throws ServletException, IOException {
		
		String tokenComBearer = request.getHeader("Authorization");
		
		if (tokenComBearer != null) {
			String token = tokenComBearer.replace("Bearer ", "");

			var tokenService = tokenJWTService.decodeJWT(token);
			String celular = tokenService.getCelular();
			Long clienteId = tokenService.getClienteId();
			String email = tokenService.getEmail();
			List<SimpleGrantedAuthority> roles = tokenService.getRoles();
			System.out.println("TESTEr" + roles);

			var authentication = new JwtAuthenticationToken(clienteId, null, tokenComBearer, roles);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
