package br.com.danielschiavo.catalog.controller.admin;

import br.com.danielschiavo.catalog.dto.request.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.shared.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Product - Admin", description = "All endpoints related with Products, for use by administrators")
public class ProductAdminController {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductMapper mapper;

	@DeleteMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Delete a Product with the provided id")
	public ResponseEntity<?> deleteProduct(@PathVariable @NotNull Long productId) {
		service.deleteProduct(productId);
		return ResponseEntity.ok(Response.success("Product deleted successfully!", null));
	}

	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Register a Product")
	public ResponseEntity<?> registerProduct(
			@RequestBody @Valid RegisterProductRequest request,
			UriComponentsBuilder uriBuilder) {
		Product registerProduct = mapper.toEntity(request);
		Product product = service.registerProduct(registerProduct);
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(Response.success("Product registered successfully!", null));
	}

	@PutMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Update a Product")
	public ResponseEntity<?> alterarProdutoPorId(
			@PathVariable Long productId,
			@RequestBody UpdateProductRequest request) {
		Product updateProduct = mapper.toEntity(request);
		Product product = service.updateProduct(productId, updateProduct);
			
		return ResponseEntity.ok(Response.success("Product updated successfully!", null));
	}

}
