package br.com.danielschiavo.customer.service.card.validators.registercard;


import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.model.entity.Card;

import java.util.List;

public interface ValidatorRegisterCard {
	
	void validar(Long customerId, RegisterCardRequest request);

}
