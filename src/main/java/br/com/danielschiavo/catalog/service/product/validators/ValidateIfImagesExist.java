package br.com.danielschiavo.catalog.service.product.validators;

import java.util.List;

import br.com.danielschiavo.catalog.dto.request.AddProductFileRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateIfImagesExist implements ValidatorRegisterProduct {

	@Autowired
	private FileReferenceService fileService;
	
	@Override
	public void validate(RegisterProductRequest request) {
		List<String> filesReferencesId = request.files().stream().map(AddProductFileRequest::fileReferenceId).toList();

		System.out.println(" TESTE " + filesReferencesId);
		filesReferencesId.forEach(fileReference -> {
			if (!fileService.fileExists(ProductService.awsS3Directory, fileReference))
				throw new ValidationException("Unable to register the product because the file: " + fileReference + " was not uploaded");
		});
	}

}
