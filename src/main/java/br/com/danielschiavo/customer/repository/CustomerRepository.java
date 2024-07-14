package br.com.danielschiavo.customer.repository;

import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Page<Customer> findAll(Pageable pageable);
	
	@Query("SELECT c FROM Cliente c WHERE c.id = :id")
	UserDetails buscarPorId(Long id);

	@Query("SELECT c FROM Cliente c WHERE c.email = :login OR c.cellphoneNumber = :login OR c.cpf = :login")
	Optional<UserDetails> findByEmailOrCelularOrCpf(String login);

	Optional<Customer> findByCpf(String number);

	@Query("SELECT c.name, c.surname, c.profilePicture FROM Cliente c WHERE c.id = :id")
	Optional<Customer> findByIdHomePage(Long id);

}
