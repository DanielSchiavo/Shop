package br.com.danielschiavo.cliente.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	
	@Query("SELECT e FROM Endereco e WHERE e.id = :enderecoId AND e.cliente = :clienteId")
	Optional<Endereco> findByClienteIdAndEnderecoId(Long clienteId, Long enderecoId);

	@Query("SELECT e FROM Endereco e WHERE e.enderecoPadrao = true AND e.cliente = :cliente")
	Optional<Endereco> findByClienteAndEnderecoPadraoTrue(Cliente cliente);

	Optional<Endereco> findByIdAndCliente(Long idEndereco, Cliente cliente);

	Optional<List<Endereco>> findAllByClienteId(Long clienteId);

	Optional<Endereco> findByIdAndClienteId(Long enderecoId, Long clienteId);

	@Modifying
	void deleteByIdAndClienteId(Long enderecoId, Long clienteId);
}
