package br.com.danielschiavo.customer.service.card;

import java.util.List;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.customer.service.card.validators.registercard.ValidatorRegisterCard;
import lombok.Setter;

@Service
@Setter
public class CardService {

	@Autowired
	private CardRepository repository;
	
	@Autowired
	private List<ValidatorRegisterCard> validators;
	
	@Transactional
	public void deleteCardById(Long cardId, Long customerId) {
		repository.deleteByIdAndClienteId(cardId, customerId);
	}
	
	public List<Card> getAllCardsByCustomerId(Long customerId) {
		return repository.findAllByClienteId(customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have any registered card"));
	}
	
	public Card getCardByIdAndCustomerId(Long cardId, Long customerId) {
		return repository.findByIdAndClienteId(cardId, customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have a card with id: " + cardId));
	}

	@Transactional
	public Card registerCard(Long customerId, Card card) {
		List<Card> cardsAlreadyRegistred = getAllCardsByCustomerId(customerId);
		validators.forEach(v -> v.validar(card, cardsAlreadyRegistred, customerId));

		if (card.getIsDefault()) {
			cardsAlreadyRegistred.stream().filter(car -> car.getIsDefault().equals(true)).forEach(car -> car.setIsDefault(false));
		}

		card.setBankName("Bank API needs to be implemented");
		cardsAlreadyRegistred.add(card);
		repository.saveAll(cardsAlreadyRegistred);
		return card;
	}
	
	@Transactional
	public void switchDefaultCardStatus(Long cardId, Long customerId) {
		List<Card> cards = getAllCardsByCustomerId(customerId);

		Card card = cards.stream()
				.filter(c -> c.getId().equals(cardId))
				.findFirst().orElseThrow(() -> new ValidationException("There's no card with provided id"));

		boolean newDefaultCardState = !card.getIsDefault(); // Inverte o state do cartão

		// Define o novo state do cartão encontrado
		card.setIsDefault(newDefaultCardState);

		// Define todos os outros cartões como não padrão, se necessário
		if (newDefaultCardState) {
			cards.stream()
					.filter(c -> !c.getId().equals(cardId) && c.getIsDefault().equals(true))
					.forEach(c -> c.setIsDefault(false));
		}

		repository.saveAll(cards);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


	
}
