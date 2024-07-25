package br.com.danielschiavo.catalog.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/product")
@Tag(name = "Product - User", description = "All endpoints related to a Product, for public use")
public class ProductUserController {

	@Autowired
	private ProductService service;

	@GetMapping
	@Operation(summary = "Get all products")
	public ResponseEntity<?> getAllProducts(Pageable pageable) {
		Page<ShowProductsResponse> response = service.getAllProducts(pageable);

		return ResponseEntity.ok(Response.success("Success recovering all products", response));
	}
	
	@GetMapping("/{productId}")
	@Operation(summary = "Get all product data by id")
	public ResponseEntity<?> detailProductById(@PathVariable Long productId) {
		DetailProductResponse response = service.getProductById(productId);
		
		return ResponseEntity.ok(Response.success("Success recovering all product data", response));
	}

}
