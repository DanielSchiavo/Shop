package br.com.danielschiavo.shared.infra.security;

import br.com.danielschiavo.shared.exception.ValidationException;
import jakarta.validation.Validation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SecurityService {

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
	
    public Long getCustomerId() {
        return (Long) getAuthentication().getPrincipal();
    }
    
    public String getTokenComBearer() {
    	var jwtAuthenticationToken = (JwtAuthenticationToken) getAuthentication();
    	return jwtAuthenticationToken.getToken();
    }

    public boolean isAdmin() {
        return getAuthentication().getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean hasSameId(Long customerId) {
        Long verdadeiroClienteId = (Long) getAuthentication().getPrincipal();
        return verdadeiroClienteId.equals(customerId);
    }

    public void verifyNeededPermission(String permission) {
        boolean match = getAuthentication().getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_" + permission));
        if (!match) {
            throw new ValidationException("User doesn't have the needed permission to access it");
        }
    }
}
