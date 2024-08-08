package br.com.danielschiavo.catalog.service.product.validators;

import java.util.List;
import java.util.stream.IntStream;

import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateFileOrdering implements ValidatorRegisterProduct {

	@Override
	public void validate(Product product) {
        List<Byte> orderedPositions = product.getProductFiles().stream()
									                .map(ProductFile::getPosition)
									                .sorted()
									                .toList();

		boolean allMatch = IntStream.range(0, orderedPositions.size())
									.allMatch(i -> i == orderedPositions.get(i));
		
		if (!allMatch) {
			throw new ValidationException("Product file positions are not following the correct ordering");
		}
	}

}
