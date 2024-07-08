package br.com.danielschiavo.cliente.repository;

import java.util.Optional;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Page<Cliente> findAll(Pageable pageable);
	
	@Query("SELECT c FROM Cliente c WHERE c.id = :id")
	UserDetails buscarPorId(Long id);

	@Query("SELECT c FROM Cliente c WHERE c.email = :login OR c.celular = :login OR c.cpf = :login")
	Optional<UserDetails> findByEmailOrCelularOrCpf(String login);

	Optional<Cliente> findByCpf(String number);

	@Query("SELECT c.nome, c.sobrenome, c.fotoPerfil FROM Cliente c WHERE c.id = :id")
	Optional<Cliente> findByIdPaginaInicial(Long id);

}
