package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.dto.request.UpdateSubCategoryRequest;
import br.com.danielschiavo.produto.dto.request.CreateSubCategoryRequest;
import br.com.danielschiavo.produto.model.entity.SubCategory;
import br.com.danielschiavo.produto.service.SubCategoryService;
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

@RestController
@RequestMapping("/admin/sub-category")
@Tag(name = "Sub Category - Admin", description = "All endpoints related to Sub Category of a product, for use by administrators")
public class SubCategoryAdminController {
	
	@Autowired
	private SubCategoryService service;
	
	@DeleteMapping("/{subCategoryId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Delete a Sub Category by id")
	public ResponseEntity<?> deleteSubCategoryById(@PathVariable Long subCategoryId){
		service.deleteSubCategoryById(subCategoryId);
		return ResponseEntity.ok(Response.success("Sub Category deleted successfully!", null));
	}
	
	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Create a Sub Category")
	public ResponseEntity<?> createSubCategory(@RequestBody @Valid CreateSubCategoryRequest request) {
		SubCategory subCategory = service.createSubCategory(request.name(), request.categoryId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Sub Category created successfully!", subCategory));
	}
	
	@PutMapping("/{subCategoryId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Update a Sub Category")
	public ResponseEntity<?> alterarSubCategoriaPorId(@PathVariable Long subCategoryId, @RequestBody UpdateSubCategoryRequest request) {
		SubCategory subCategory = service.updateSubCategory(subCategoryId, request.name(), request.categoryId());
		
		return ResponseEntity.ok(Response.success("Sub Category updated successfully!", null));
	}
}
