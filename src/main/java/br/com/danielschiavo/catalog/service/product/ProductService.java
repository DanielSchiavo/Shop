package br.com.danielschiavo.catalog.service.product;

import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.repository.ProductRepository;
import br.com.danielschiavo.catalog.service.product.validators.registerproduct.ValidatorRegisterProduct;
import br.com.danielschiavo.filestorage.service.FileService;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private FileService fileService;

	@Autowired
	private ProductMapper mapper;

	@Autowired
	private List<ValidatorRegisterProduct> validators;

	public static final String bucketName = "product";
	
	@Transactional
	public void deleteProduct(Long productId) {
		Product product = getProductById(productId);
		product.getAllImageNames().forEach(image -> fileService.deleteFile(bucketName, image));

		repository.delete(product);
	}
	
	@Transactional
	public Product registerProduct(Product registerProduct) {
		validators.forEach(v -> v.validate(registerProduct));
		
		return repository.save(registerProduct);
	}

	@Transactional
	public Product updateProduct(Long id, Product updateProduct) {
		Product product = getProductById(id);
		mapper.updateProduct(updateProduct, product);
		return repository.save(product);
	}

	public Page<Product> getAllProducts(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Product getProductById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ValidationException("There's no product with id: " + id));
	}

	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


}
