package br.com.danielschiavo.shop.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class TokenJWTService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(Cliente cliente) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API Shop")
		        .withSubject(cliente.getEmail())
		        .withClaim("id", cliente.getId())
		        .withClaim("email", cliente.getEmail())
		        .withClaim("celular", cliente.getCelular())
		        .withExpiresAt(expirationDate())
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro ao gerar token de autenticacao");
		}
	}
	
	public Long getClaimIdJWT(String token) {
		return decodeJWT(token).getClaim("id").asLong();
	}
	
	public DecodedJWT decodeJWT(String tokenJWT) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.require(algorithm)
		        .withIssuer("API Shop")
		        .build()
		        .verify(tokenJWT);
		} catch (JWTVerificationException exception){
			throw new RuntimeException("Token JWT inválido ou expirado!");
		}
	}
	
	public String getSubject(String tokenJWT) {
		return decodeJWT(tokenJWT).getSubject();
	}

	private Instant expirationDate() {
		return LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
