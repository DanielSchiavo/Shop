package br.com.danielschiavo.customer.service.card.validators.registercard;

import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;


@Service
public class ValidateCardLimit implements ValidatorRegisterCard {

	private int limitOfCards = 10;

	@Autowired
	private CardRepository repository;

	@Override
	public void validar(Long customerId, RegisterCardRequest request) {
		Card probe = new Card();
		probe.setCustomerId(customerId);

		Example<Card> example = Example.of(probe);
		long count = repository.count(example);

		if (count == limitOfCards) {
			throw new ValidationException("Limit on number of cards per customer reached, which is " + limitOfCards + " cards");
		}
	}

}
