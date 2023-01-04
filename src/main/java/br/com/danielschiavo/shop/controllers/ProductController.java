package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.product.DetailingProductDTO;
import br.com.danielschiavo.shop.models.product.Product;
import br.com.danielschiavo.shop.models.product.ProductDTO;
import br.com.danielschiavo.shop.models.product.ShowProductsDTO;
import br.com.danielschiavo.shop.models.product.UpdateProductDTO;
import br.com.danielschiavo.shop.services.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("products")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DetailingProductDTO> registerProduct(@RequestBody @Valid ProductDTO dto, UriComponentsBuilder uriBuilder){
		var product = new Product(dto);
		service.save(product);
		
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DetailingProductDTO(product));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DetailingProductDTO> updateById(@PathVariable Long id, @RequestBody UpdateProductDTO updateProductDTO){
		var product = service.getReferenceById(id);
		product.updateAttributes(updateProductDTO);
		
		return ResponseEntity.ok(new DetailingProductDTO(product));
	}
	
	@PutMapping("/{id}/active")
	@Transactional
	public ResponseEntity<DetailingProductDTO> changeActive(@PathVariable Long id){
		var product = service.getReferenceById(id);
		product.changeActive();
		
		return ResponseEntity.ok(new DetailingProductDTO(product));
	}
	
	@GetMapping
	public ResponseEntity<Page<ShowProductsDTO>> listAllPageable(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable){		
		var page = service.findAllByActiveTrue(pageable);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> datailingById(@PathVariable Long id){
		var product = service.getReferenceById(id);
		return ResponseEntity.ok(new ProductDTO(product));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
