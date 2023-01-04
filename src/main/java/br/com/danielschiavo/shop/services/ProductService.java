package br.com.danielschiavo.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.product.Product;
import br.com.danielschiavo.shop.models.product.ShowProductsDTO;
import br.com.danielschiavo.shop.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;

	public void save(Product product) {
		repository.save(product);
	}

	public Product getReferenceById(Long id) {
		return repository.getReferenceById(id);
	}

	public Page<ShowProductsDTO> findAllByActiveTrue(Pageable pageable) {
		return repository.findAllByActiveTrue(pageable).map(ShowProductsDTO::new);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
