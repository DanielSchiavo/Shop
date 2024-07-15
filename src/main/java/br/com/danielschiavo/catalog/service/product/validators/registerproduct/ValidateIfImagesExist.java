package br.com.danielschiavo.catalog.service.product.validators.registerproduct;

import java.util.List;

import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateIfImagesExist implements ValidatorRegisterProduct {

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;
	
	@Override
	public void validate(Product product) {
		List<String> nomes = product.getProductFiles().stream().map(ProductFile::getName).toList();

		nomes.forEach(name -> {
			boolean existe = fileStorageProdutoService.verificarSeImagemExiste(name);
			if (!existe)
				throw new ValidationException("Unable to register the product because the image: " + name + " was not uploaded");
		});
	}

}
