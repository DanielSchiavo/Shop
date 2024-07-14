package br.com.danielschiavo.customer.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface CardRepository extends JpaRepository<Card, Long>{

	Optional<Card> findByNumeroCartaoAndTipoCartaoAndClienteId(String numeroCartao, CardType cardType, Long clienteId);

	Optional<Card> findByIdAndClienteId(Long cartaoId, Long clienteId);

	Optional<List<Card>> findAllByClienteId(Long clienteId);

	@Modifying
	void deleteByIdAndClienteId(Long cartaoId, Long clienteId);
}
