package br.com.danielschiavo.customer.service.card.validators.registercard;

import java.util.List;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateCardLimit implements ValidatorRegisterCard {

	private int limitOfCards = 10;

	@Override
	public void validar(Card card, List<Card> allCardsAlreadyRegistered, Long customerId) {
		long quantidadeCartoes = allCardsAlreadyRegistered.size();

		if (quantidadeCartoes == limitOfCards) {
			throw new ValidationException("Limit on number of cards per customer reached, which is " + limitOfCards + " cards");
		}
	}

}
