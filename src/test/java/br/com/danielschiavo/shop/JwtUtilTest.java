package br.com.danielschiavo.shop;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class JwtUtilTest {

	private static final String secret = "123456";

	public static String generateTokenAdmin() {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API Shop")
		        .withSubject("daniel.schiavo35@gmail.com")
		        .withClaim("id", 1L)
		        .withExpiresAt(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00")))
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro ao gerar token de autenticacao");
		}
	}
	
	public static String generateTokenUser() {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API Shop")
		        .withSubject("silvana.dasilva@gmail.com")
		        .withClaim("id", 2L)
		        .withExpiresAt(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00")))
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro ao gerar token de autenticacao");
		}
	}
}
