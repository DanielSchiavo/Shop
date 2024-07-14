package br.com.danielschiavo.customer.service.card.validators.registercard;


import br.com.danielschiavo.customer.model.entity.Card;

import java.util.List;

public interface ValidatorRegisterCard {
	
	void validar(Card card, List<Card> allCardsAlreadyRegistered, Long customerId);

}
