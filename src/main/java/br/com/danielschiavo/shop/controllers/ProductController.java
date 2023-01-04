package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.Product;
import br.com.danielschiavo.shop.models.dto.DetailingProductDTO;
import br.com.danielschiavo.shop.models.dto.ProductDTO;
import br.com.danielschiavo.shop.models.dto.ShowProductsDTO;
import br.com.danielschiavo.shop.models.dto.UpdateProductDTO;
import br.com.danielschiavo.shop.repositories.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("products")
public class ProductController {

	@Autowired
	private ProductRepository repository;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DetailingProductDTO> register(@RequestBody @Valid ProductDTO dto, UriComponentsBuilder uriBuilder){
		var product = new Product(dto);
		repository.save(product);
		
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DetailingProductDTO(product));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DetailingProductDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateProductDTO updateProductDTO){
		var product = repository.getReferenceById(id);
		product.updateAttributes(updateProductDTO);
		
		return ResponseEntity.ok(new DetailingProductDTO(product));
	}
	
	@PutMapping("/{id}/active")
	@Transactional
	public ResponseEntity<DetailingProductDTO> changeActive(@PathVariable Long id){
		var product = repository.getReferenceById(id);
		product.changeActive();
		
		return ResponseEntity.ok(new DetailingProductDTO(product));
	}
	
	@GetMapping
	public ResponseEntity<Page<ShowProductsDTO>> list(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable){
		var page = repository.findAllByActiveTrue(pageable).map(ShowProductsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> datailing(@PathVariable Long id){
		var product = repository.getReferenceById(id);
		
		return ResponseEntity.ok(new ProductDTO(product));
	}
}
