package br.com.danielschiavo.catalog.service.product.validators;

import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;

public interface ValidatorRegisterProduct {
	
	void validate(RegisterProductRequest request);

}
