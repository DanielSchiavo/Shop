package br.com.danielschiavo.shared.infra.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private String token;
	
	private final Object principal;

	private Object credentials;
	
    public JwtAuthenticationToken(Object principal, Object credentials, String token,
            					  Collection<? extends GrantedAuthority> authorities) {
    	super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.token = token;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	public String getToken() {
		return this.token;
	}

}
