package br.com.danielschiavo.catalog.service.product.validators.registerproduct;

import java.util.List;

import br.com.danielschiavo.catalog.dto.request.AddProductFileRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.service.FileService;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateIfImagesExist implements ValidatorRegisterProduct {

	@Autowired
	private FileService fileService;
	
	@Override
	public void validate(RegisterProductRequest request) {
		List<String> nomes = request.files().stream().map(AddProductFileRequest::name).toList();

		nomes.forEach(fileName -> {
			boolean existe = fileService.checkIfFileExists(ProductService.bucketName, fileName);
			if (!existe)
				throw new ValidationException("Unable to register the product because the image: " + fileName + " was not uploaded");
		});
	}

}
