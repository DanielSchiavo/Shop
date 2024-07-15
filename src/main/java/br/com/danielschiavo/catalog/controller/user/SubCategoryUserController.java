package br.com.danielschiavo.produto.controller.user;

import br.com.danielschiavo.produto.model.entity.SubCategory;
import br.com.danielschiavo.produto.service.SubCategoryService;
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
@RequestMapping
@Tag(name = "Sub Category - User", description = "All endpoints related to a Sub Category, for public use")
public class SubCategoryUserController {
	
	@Autowired
	private SubCategoryService service;
	
	@GetMapping("/public/sub-category")
	@Operation(summary = "Get all existing Sub Categories")
	public ResponseEntity<?> getAllSubCategories(Pageable pageable){
		Page<SubCategory> allSubCategories = service.getAllSubCategories(pageable);
		return ResponseEntity.ok(allSubCategories);
	}
	
}
