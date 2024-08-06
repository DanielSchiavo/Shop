package br.com.danielschiavo.catalog.service.product.validators;

import java.util.Optional;

import br.com.danielschiavo.catalog.dto.request.AddProductFileRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateFirstFileIsImage implements ValidatorRegisterProduct {

	@Override
	public void validate(RegisterProductRequest request) {
		Optional<AddProductFileRequest> first = request.files().stream().filter(arq -> arq.position() == 0).findFirst();
		if (first.isPresent()) {
			String fileReferenceId = first.get().fileReferenceId();
			if(!fileReferenceId.endsWith(".jpeg") && !fileReferenceId.endsWith(".png") && !fileReferenceId.endsWith(".jpg")) {
				throw new ValidationException("The file at position 0 of the product must always be an image");
			}
		} else {
			throw new ValidationException("There's no image on the first position");
		}
	}

}
