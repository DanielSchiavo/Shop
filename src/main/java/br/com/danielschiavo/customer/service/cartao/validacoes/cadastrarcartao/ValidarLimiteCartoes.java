package br.com.danielschiavo.customer.service.cartao.validacoes.cadastrarcartao;

import java.util.List;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidarLimiteCartoes implements ValidatorRegisterCard {

	@Override
	public void validar(Card card, List<Card> cartoesJaCadastrados, Long clienteId) {
		long quantidadeCartoes = cartoesJaCadastrados.size();

		if (quantidadeCartoes == 10) {
			throw new ValidacaoException("Limite de quantidade de cartões por customer atingido, que são 10 cartões");
		}
	}

}
