package br.com.danielschiavo.catalog.service.product;

import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.dto.request.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import br.com.danielschiavo.catalog.repository.ProductRepository;
import br.com.danielschiavo.catalog.service.product.validators.ValidatorRegisterProduct;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ProductMapper mapper;

	@Autowired
	private List<ValidatorRegisterProduct> validators;

	public static final String awsS3Directory = "products";

	@Transactional
	public List<String> deleteProduct(Long productId) {
		Product product = repository.findById(productId)
				.orElseThrow(() -> new ValidationException("Could not delete product because there's no product with id: " + productId));

		List<String> productFilesNames = product.getProductFiles().stream()
				.filter(pf -> pf.getType().equals(ProductFileType.IMAGE))
				.map(pf -> awsS3Directory + pf.getFileName())
				.collect(Collectors.toList());

		repository.deleteById(productId);
		return productFilesNames;
	}
	
	@Transactional
	public DetailProductResponse registerProduct(RegisterProductRequest request) {
		validators.forEach(v -> v.validate(request));

		Product product = mapper.toEntity(request);

		return mapper.toDetailProduct(repository.save(product));
	}

	@Transactional
	public DetailProductResponse updateProduct(Long productId, UpdateProductRequest request) {
		Product product = repository.findById(productId)
				.orElseThrow(() -> new ValidationException("Cannot update product because there's no product with given id: " + productId));

		mapper.updateProduct(request, product);

		return mapper.toDetailProduct(repository.save(product));
	}

	public List<ShowProductsResponse> getAllProducts(Pageable pageable) {
		Page<Product> all = repository.findAll(pageable);

		return all.getContent().stream()
				.map(mapper::toShowProducts).collect(Collectors.toList());
	}

	public DetailProductResponse getProductById(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new ValidationException("There's no product with id: " + id));
		return mapper.toDetailProduct(product);
	}

	public List<ShowProductsResponse> getProductsById(List<Long> ids) {
		List<Product> allById = repository.findAllById(ids);

		return mapper.toShowProducts(allById);
	}

	public void checkIfProductExist(Long productId) {
		boolean exists = repository.existsById(productId);
		if (!exists) {
			throw new ValidationException("Product does not exist!");
		}
	}


//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


}
