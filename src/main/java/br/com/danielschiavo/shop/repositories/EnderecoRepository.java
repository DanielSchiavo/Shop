package br.com.danielschiavo.shop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.danielschiavo.shop.models.endereco.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	
	@Query("SELECT e FROM Endereco e WHERE e.id = :enderecoId AND e.cliente = :clienteId")
	Optional<Endereco> findByCliente_idAndEnderecoId(Long clienteId, Long enderecoId);

}
