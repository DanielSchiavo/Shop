package br.com.danielschiavo.catalog.service.product.validators.registerproduct;


import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.repository.ProductRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ValidateProductNameAlreadyExist implements ValidatorRegisterProduct {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public void validate(RegisterProductRequest request) {
		Optional<Product> optionalProduct = productRepository.findByNomeLowerCase(request.name());
		if (optionalProduct.isPresent()) {
			throw new ValidationException("There is already a product with that name");
		}
	}

}
