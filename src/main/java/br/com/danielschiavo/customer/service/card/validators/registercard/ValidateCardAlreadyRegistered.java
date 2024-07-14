package br.com.danielschiavo.customer.service.card.validators.registercard;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ValidateCardAlreadyRegistered implements ValidatorRegisterCard {

	@Autowired
	private CardRepository cardRepository;
	
	@Override
	public void validar(Card card, List<Card> allCardsAlreadyRegistered, Long customerId) {
		boolean match = allCardsAlreadyRegistered.stream()
				.anyMatch(car -> card.getCardNumber().equals(car.getCardNumber()) && card.getCardType().equals(car.getCardType()));
		if (match) {
			throw new ValidationException("You already owns a card with provided number and type");
		}
	}

}
