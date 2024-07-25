package br.com.danielschiavo.catalog.service.product.validators.registerproduct;

import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.model.entity.Product;

public interface ValidatorRegisterProduct {
	
	void validate(RegisterProductRequest request);

}
