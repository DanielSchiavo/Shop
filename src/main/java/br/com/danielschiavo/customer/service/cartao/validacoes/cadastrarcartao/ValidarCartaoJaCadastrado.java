package br.com.danielschiavo.customer.service.cartao.validacoes.cadastrarcartao;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ValidarCartaoJaCadastrado implements ValidatorRegisterCard {

	@Autowired
	private CardRepository cardRepository;
	
	@Override
	public void validar(Card card, List<Card> cartoesJaCadastrados, Long clienteId) {
		boolean match = cartoesJaCadastrados.stream()
				.anyMatch(car -> card.getCardNumber().equals(car.getCardNumber()) && card.getCardType().equals(car.getCardType()));
		if (match) {
			throw new ValidacaoException("O usuário já possui um cartão com esse número e cardType cadastrado");
		}
	}

}
