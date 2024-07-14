package br.com.danielschiavo.customer.service.cartao.validacoes.cadastrarcartao;


import br.com.danielschiavo.customer.model.entity.Card;

import java.util.List;

public interface ValidatorRegisterCard {
	
	void validar(Card card, List<Card> cartoes, Long clienteId);

}
