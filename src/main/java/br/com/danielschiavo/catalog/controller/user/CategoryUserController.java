package br.com.danielschiavo.produto.controller.user;

import br.com.danielschiavo.produto.model.entity.Category;
import br.com.danielschiavo.produto.service.CategoryService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/categories")
@Tag(name = "Category - User", description = "All endpoints related to Categories, for public use")
public class CategoryUserController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	@Operation(summary = "Get all existing Categories")
	public ResponseEntity<?> getAllCategories(Pageable pageable){
		Page<Category> pageCategories = service.getAllCategories(pageable);
		return ResponseEntity.ok(Response.success("Success recovering all categories", pageCategories));
	}
}
