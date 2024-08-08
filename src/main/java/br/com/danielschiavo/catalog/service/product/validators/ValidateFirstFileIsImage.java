package br.com.danielschiavo.catalog.service.product.validators;

import java.util.Optional;

import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateFirstFileIsImage implements ValidatorRegisterProduct {

	@Override
	public void validate(Product product) {
		Optional<ProductFile> first = product.getProductFiles().stream().filter(arq -> arq.getPosition() == 0).findFirst();
		if (first.isPresent()) {
			String fileName = first.get().getFileName();
			if(!fileName.endsWith(".jpeg") && !fileName.endsWith(".png") && !fileName.endsWith(".jpg")) {
				throw new ValidationException("The file at position 0 of the product must always be an image");
			}
		} else {
			throw new ValidationException("There's no image on the first position");
		}
	}

}
