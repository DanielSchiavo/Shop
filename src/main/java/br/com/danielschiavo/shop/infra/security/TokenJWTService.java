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

@Service
public class TokenJWTService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	private String token;

	public void setTokenJWT(String tokenJWT) {
		this.token = tokenJWT;
	}
	
	public String getTokenJWT() {
		return this.token;
	}
	
	public String generateToken(Cliente client) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API Shop")
		        .withSubject(client.getEmail())
		        .withClaim("id", client.getId())
//		        .withClaim("email", client.getEmail())
		        .withExpiresAt(expirationDate())
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro ao gerar token de autenticacao");
		}
	}
	
	
	public Long getClaimIdJWT() {
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
	
	public String getSubject() {
		return decodeJWT(this.token).getSubject();
	}

	private Instant expirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
