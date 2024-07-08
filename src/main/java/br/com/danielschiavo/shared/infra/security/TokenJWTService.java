package br.com.danielschiavo.shared.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class TokenJWTService {
	
	@Value("${api.security.token.secret}")
	private String secret;

	@Value("${api.security.token.issuer}")
	private String issuer;

	private Algorithm algorithm = Algorithm.HMAC256(secret);

	private DecodedJWT decodedJWT;
	
	public String generateToken(Cliente cliente) {
		try {
			List<String> roles = cliente.getRoles().stream().map(r -> r.getRole().toString()).collect(Collectors.toList());

		    return JWT.create()
		        .withIssuer(issuer)
		        .withSubject(cliente.getId().toString())
		        .withClaim("email", cliente.getEmail())
		        .withClaim("celular", cliente.getCelular())
				.withClaim("roles", roles)
		        .withExpiresAt(expirationDate())
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new ValidacaoException("Erro ao gerar token de autenticacao");
		}
	}
	
	public TokenJWTService decodeJWT(String tokenJWT) {
		try {
		    decodedJWT = JWT.require(algorithm)
		        .withIssuer(issuer)
		        .build()
		        .verify(tokenJWT);

			return this;
		} catch (JWTVerificationException exception){
			throw new ValidacaoException("Token JWT inválido ou expirado!");
		}
	}

	public Long getClienteId() {
		return Long.valueOf(decodedJWT.getSubject());
	}

	public String getCelular() {
		return decodedJWT.getClaim("celular").toString();
	}

	public String getEmail() {
		return decodedJWT.getClaim("email").toString();
	}

	public List<SimpleGrantedAuthority> getRoles() {
		List<SimpleGrantedAuthority> roles = decodedJWT.getClaim("roles").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		return roles;
	}

	private Instant expirationDate() {
		return LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
