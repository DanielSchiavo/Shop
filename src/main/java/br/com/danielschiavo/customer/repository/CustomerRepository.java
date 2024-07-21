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
	
	@Query("SELECT c FROM Customer c WHERE c.email = :login OR c.cellphoneNumber = :login OR c.cpf = :login")
	Optional<UserDetails> findByEmailOrCellphoneNumberOrCpf(String login);

	Optional<Customer> findByCpf(String number);

	@Query("SELECT c.name, c.surname, c.profilePicture FROM Customer c WHERE c.id = :id")
	Optional<Customer> findByIdHomePage(Long id);

}
