package br.com.danielschiavo.customer.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface CardRepository extends JpaRepository<Card, Long>{

	Optional<Card> findByIdAndCustomerId(Long cardId, Long customerId);

	Optional<List<Card>> findAllByCustomerId(Long customerId);

	@Modifying
	void deleteByIdAndCustomerId(Long cardId, Long customerId);
}
