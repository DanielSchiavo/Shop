package br.com.danielschiavo.catalog.controller.admin;

import br.com.danielschiavo.catalog.dto.request.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.catalog.service.CategoryService;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.service.FileService;
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

import java.util.Set;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Product - Admin", description = "All endpoints related with Products, for use by administrators")
public class ProductAdminController {

	@Autowired
	private ProductService service;

	@Autowired
	private FileService fileService;

	@Autowired
	private CategoryService categoryService;

	@DeleteMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Delete a Product with the provided id")
	public ResponseEntity<?> deleteProduct(@PathVariable @NotNull Long productId) {
		Set<ProductFile> productFiles = service.deleteProduct(productId);
		productFiles.forEach(image -> fileService.deleteFile(ProductService.bucketName, image.getName()));
		return ResponseEntity.ok(Response.success("Product deleted successfully!", null));
	}

	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Register a Product")
	public ResponseEntity<?> registerProduct(
			@RequestBody @Valid RegisterProductRequest request,
			UriComponentsBuilder uriBuilder) {
		categoryService.validateCategoryExists(request.categoryId());
		DetailProductResponse response = service.registerProduct(request);
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(Response.success("Product registered successfully!", null));
	}

	@PutMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Update a Product")
	public ResponseEntity<?> updateProductById(
			@PathVariable Long productId,
			@RequestBody UpdateProductRequest request) {
		categoryService.validateCategoryExists(request.categoryId());
		DetailProductResponse response = service.updateProduct(productId, request);
			
		return ResponseEntity.ok(Response.success("Product updated successfully!", null));
	}

}
