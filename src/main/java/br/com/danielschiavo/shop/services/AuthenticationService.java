package br.com.danielschiavo.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.repositories.ClienteRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private ClienteRepository clientRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return clientRepository.findByEmail(email);
	}

}
