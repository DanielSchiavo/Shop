package br.com.danielschiavo.shop.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.cliente.Cliente;

@Service
public class UsuarioAutenticadoService {

    public UserDetails getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
    
    public String getUsernameUsuarioAutenticado() {
        UserDetails userDetails = getUsuarioAutenticado();
        return userDetails != null ? userDetails.getUsername() : null;
    }
    
    public Cliente getCliente() {
        UserDetails userDetails = getUsuarioAutenticado();
        return userDetails != null ? (Cliente) userDetails : null;
    }
}
