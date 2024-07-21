package br.com.danielschiavo.customer.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface AddressRepository extends JpaRepository<Address, Long>{
	
	Optional<List<Address>> findAllByCustomerId(Long customerId);

	Optional<Address> findByIdAndCustomerId(Long addressId, Long customerId);

	@Modifying
	void deleteByIdAndCustomerId(Long addressId, Long customerId);
}
