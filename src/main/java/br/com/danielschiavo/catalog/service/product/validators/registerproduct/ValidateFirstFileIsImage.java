package br.com.danielschiavo.produto.service.product.validators.registerproduct;

import java.util.Optional;

import br.com.danielschiavo.produto.model.entity.Product;
import br.com.danielschiavo.produto.model.valueobject.ProductFile;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateFirstFileIsImage implements ValidatorRegisterProduct {

	@Override
	public void validate(Product cadastrarProduto) {
		Optional<ProductFile> first = cadastrarProduto.getProductFiles().stream().filter(arq -> arq.getPosition() == 0).findFirst();
		if (first.isPresent()) {
			String nomeArquivo = first.get().getName();
			if(!nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpg")) {
				throw new ValidationException("The file at position 0 of the product must always be an image");
			}
		}
	}

}
