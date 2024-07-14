package br.com.danielschiavo.shared.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SecurityService {

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
    public Long getCustomerId() {
        return (Long) authentication.getPrincipal();
    }
    
    public String getTokenComBearer() {
    	var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
    	return jwtAuthenticationToken.getToken();
    }

    public boolean isAdmin() {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean hasSameId(Long customerId) {
        Long verdadeiroClienteId = (Long) authentication.getPrincipal();
        return verdadeiroClienteId.equals(customerId);
    }
}
