package br.com.danielschiavo.customer.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface AddressRepository extends JpaRepository<Address, Long>{
	
	@Query("SELECT e FROM Address e WHERE e.id = :enderecoId AND e.cliente = :clienteId")
	Optional<Address> findByClienteIdAndEnderecoId(Long clienteId, Long enderecoId);

	@Query("SELECT e FROM Address e WHERE e.isDefault = true AND e.cliente = :cliente")
	Optional<Address> findByClienteAndEnderecoPadraoTrue(Customer customer);

	Optional<Address> findByIdAndCliente(Long idEndereco, Customer customer);

	Optional<List<Address>> findAllByCustomerId(Long clienteId);

	Optional<Address> findByIdAndCustomerId(Long enderecoId, Long clienteId);

	@Modifying
	void deleteByIdAndCustomerId(Long enderecoId, Long clienteId);
}
