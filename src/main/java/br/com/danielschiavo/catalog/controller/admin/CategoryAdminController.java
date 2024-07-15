package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.model.entity.Category;
import br.com.danielschiavo.produto.dto.request.CreateCategoryRequest;
import br.com.danielschiavo.produto.service.CategoryService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/category")
@Tag(name = "Category - Admin", description = "All endpoints related to Category of a product, for use by administrators")
public class CategoryAdminController {
	
	@Autowired
	private CategoryService service;
	
	@DeleteMapping("/{categoryId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Delete a Category by id and all Sub Categories related to it")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Long categoryId) {
		service.deleteCategoryById(categoryId);
		return ResponseEntity.ok(Response.success("Category deleted successfully!", null));
	}
	
	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Create a Category")
	public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
		Category category = service.createCategory(request.name());
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Category created successfully!", category));
	}
	
	@PutMapping("/{categoryId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Update a Category")
	public ResponseEntity<?> alterarNomeCategoriaPorId(@PathVariable Long categoryId, @RequestBody @NotNull CreateCategoryRequest request) {
		Category category = service.updateCategory(categoryId, request.name());

		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Category updated successfully", null));
	}
}
