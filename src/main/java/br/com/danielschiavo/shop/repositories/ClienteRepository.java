package br.com.danielschiavo.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.danielschiavo.shop.models.cliente.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Page<Cliente> findAll(Pageable pageable);

	UserDetails findByEmail(String email);

}
