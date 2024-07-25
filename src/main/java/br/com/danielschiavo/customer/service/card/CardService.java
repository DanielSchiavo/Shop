package br.com.danielschiavo.customer.service.card;

import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.dto.response.card.ShowCardResponse;
import br.com.danielschiavo.customer.mapper.CardMapper;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
	private CardMapper mapper;
	
	@Autowired
	private List<ValidatorRegisterCard> validators;
	
	@Transactional
	public void deleteCardById(Long cardId, Long customerId) {
		Card probe = new Card();
		probe.setId(cardId);
		probe.setCustomerId(customerId);
		Example<Card> example = Example.of(probe);
		if (!repository.exists(example)) {
			throw new ValidationException("Could not delete this card, contact an administrator");
		}
		repository.deleteByIdAndCustomerId(cardId, customerId);
	}
	
	public List<ShowCardResponse> getAllCardsByCustomerId(Long customerId) {
		List<Card> cards = repository.findAllByCustomerId(customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have any registered card"));

		return mapper.toListShowCard(cards);
	}
	
	public DetailCardResponse getCardByIdAndCustomerId(Long cardId, Long customerId) {
		Card card = repository.findByIdAndCustomerId(cardId, customerId)
				.orElseThrow(() -> new ValidationException("Could not get card data, contact an administrator"));

		return mapper.toDetailCard(card);
	}

	@Transactional
	public DetailCardResponse registerCard(Long customerId, RegisterCardRequest request) {
		validators.forEach(v -> v.validar(customerId, request));

		Card registerCard = mapper.toEntity(request, customerId);

		if (request.isDefault()) {
			defineOtherCardAsIsDefaultFalse(customerId);
		}

		registerCard.setBankName("Bank API needs to be implemented");
		return mapper.toDetailCard(repository.save(registerCard));
	}
	
	@Transactional
	public void switchIsDefaultStatus(Long cardId, Long customerId) {
		Card card = repository.findByIdAndCustomerId(cardId, customerId)
				.orElseThrow(() -> new ValidationException("Could not switch default status, contact an administrator"));

		if (card.getIsDefault()) {
			card.setIsDefault(false);
		} else {
			defineOtherCardAsIsDefaultFalse(customerId);
			card.setIsDefault(true);
		}

		repository.save(card);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public void defineOtherCardAsIsDefaultFalse(Long customerId) {
		Card probe = new Card();
		probe.setCustomerId(customerId);
		probe.setIsDefault(true);

		Example<Card> example = Example.of(probe);

		repository.findOne(example).ifPresent(card -> {
			card.setIsDefault(false);
			repository.save(card);
		});
	}
	
}
