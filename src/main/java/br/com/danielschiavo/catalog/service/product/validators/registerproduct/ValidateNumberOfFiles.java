package br.com.danielschiavo.catalog.service.product.validators.registerproduct;

import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateNumberOfFiles implements ValidatorRegisterProduct {

	private final int MAX_FILES = 10;
	
	@Override
	public void validate(Product product) {
		if (product.getProductFiles().size() > MAX_FILES) {
			throw new ValidationException("The maximum number of files is: " + MAX_FILES);
		}
	}


}
