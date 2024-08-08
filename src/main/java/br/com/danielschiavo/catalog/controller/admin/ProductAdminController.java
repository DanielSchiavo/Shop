package br.com.danielschiavo.catalog.controller.admin;

import br.com.danielschiavo.catalog.dto.request.product.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.request.product.RegisterProductRequest;
import br.com.danielschiavo.catalog.dto.response.product.DetailProductResponse;
import br.com.danielschiavo.catalog.service.category.CategoryService;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin/products")
@Tag(name = "Product - Admin", description = "All endpoints related with Products, for use by administrators")
public class ProductAdminController {

	@Autowired
	private ProductService service;

	@Autowired
	private FileReferenceService fileService;

	@Autowired
	private CategoryService categoryService;

	@DeleteMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Delete a Product with the provided id")
	public ResponseEntity<?> deleteProduct(@PathVariable @NotNull Long productId) {
		List<String> filesPath = service.deleteProduct(productId);

		fileService.deleteAll(ProductService.awsS3Directory, filesPath);

		return ResponseEntity.ok(Response.success("Product deleted successfully!", null));
	}

	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Register a Product")
	public ResponseEntity<?> registerProduct(
			@RequestBody @Valid RegisterProductRequest request,
			UriComponentsBuilder uriBuilder) {
		categoryService.categoryExists(request.categoryId());

		request.files().forEach(file -> {
			boolean exists = fileService.fileExists(ProductService.awsS3Directory, file.fileReferenceId());
			if (!exists) {
				throw new ValidationException("Unable to register the product because the file: " + file.fileReferenceId() + " was not uploaded");
			}
		});

		DetailProductResponse response = service.registerProduct(request);
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(uri).body(Response.success("Product registered successfully!", null));
	}

	@PutMapping("/{productId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Update a Product")
	public ResponseEntity<?> updateProductById(
			@PathVariable Long productId,
			@RequestBody UpdateProductRequest request) {
		categoryService.categoryExists(request.categoryId());

		request.files().forEach(file -> {
			boolean exists = fileService.fileExists(ProductService.awsS3Directory, file.fileReferenceId());
			if (!exists) {
				throw new ValidationException("Unable to register the product because the file: " + file.fileReferenceId() + " was not uploaded");
			}
		});

		DetailProductResponse response = service.updateProduct(productId, request);
			
		return ResponseEntity.ok(Response.success("Product updated successfully!", null));
	}

}
