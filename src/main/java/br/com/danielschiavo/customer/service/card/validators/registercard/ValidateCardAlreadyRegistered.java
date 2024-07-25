package br.com.danielschiavo.customer.service.card.validators.registercard;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ValidateCardAlreadyRegistered implements ValidatorRegisterCard {

	@Autowired
	private CardRepository repository;
	
	@Override
	public void validar(Long customerId, RegisterCardRequest request) {
		Card probe = new Card();
		probe.setCardNumber(request.cardNumber());
		probe.setCardType(request.cardType());

		Example<Card> example = Example.of(probe);

		boolean exists = repository.exists(example);

		if (exists) {
			throw new ValidationException("You already owns a card with provided number and type");
		}
	}

}
