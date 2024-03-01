package br.com.danielschiavo.shop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;

public interface CartaoRepository extends JpaRepository<Cartao, Long>{

	Page<Cartao> findAllByCliente(Cliente cliente, Pageable pageable);

	Cartao findByCliente(Cliente cliente);

//	@Query("SELECT c FROM Cartao c WHERE c.cartaoPadrao = true AND c.cliente = :cliente")
	Optional<Cartao> findByClienteAndCartaoPadraoTrue(Cliente cliente);

	Optional<Cartao> findByNumeroCartao(String numeroCartao);

	Optional<Cartao> findByCartaoPadraoTrue();

	Boolean existsByClienteAndCartaoPadraoTrue(Cliente cliente);

	Optional<Cartao> findByIdAndCliente(Long idCartao, Cliente cliente);
}
