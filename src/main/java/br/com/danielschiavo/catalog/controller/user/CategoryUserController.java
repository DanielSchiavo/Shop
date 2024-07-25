package br.com.danielschiavo.catalog.controller.user;

import br.com.danielschiavo.catalog.dto.response.CategoryDto;
import br.com.danielschiavo.catalog.dto.response.DetailCategoryResponse;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.service.CategoryService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/public/categories")
@Tag(name = "Category - User", description = "All endpoints related to Categories, for public use")
public class CategoryUserController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/{categoryName}")
	@Operation(summary = "Get all existing Categories starting with provided name")
	public ResponseEntity<?> getAllCategories(@PathVariable String categoryName){
		List<DetailCategoryResponse> response = service.getAllCategoriesByName(categoryName);
		return ResponseEntity.ok(Response.success("Success recovering all starting with provided name", response));
	}

	@GetMapping("/hierarchical")
	@Operation(summary = "Get all existing categories hierarchicaly")
	public ResponseEntity<?> getAllCategoriesHierarchical(){
		List<CategoryDto> allHierarchical = service.getAllRootCategoriesAndItsChildren();

		return ResponseEntity.ok(Response.success("Success recovering all categories", allHierarchical));
	}

	@GetMapping("/{categoryId}")
	@Operation(summary = "Get a category by id and their sub categories")
	public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId){
		List<CategoryDto> allDto = service.getRootCategoryByIdAndItsChildren(categoryId);
		return ResponseEntity.ok(Response.success("Success recovering all categories", allDto));
	}
}
