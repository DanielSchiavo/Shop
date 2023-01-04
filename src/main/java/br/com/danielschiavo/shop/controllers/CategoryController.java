package br.com.danielschiavo.shop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.danielschiavo.shop.models.category.Category;
import br.com.danielschiavo.shop.models.category.CategoryDTO;
import br.com.danielschiavo.shop.models.category.DetailingCategoryDTO;
import br.com.danielschiavo.shop.services.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("category")
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DetailingCategoryDTO> registerCategory(@RequestBody @Valid CategoryDTO dto, UriComponentsBuilder uriBuilder) {
		var category = new Category(dto);
		service.save(category);
		
		var uri = uriBuilder.path("/category/{id}").buildAndExpand(category.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DetailingCategoryDTO(category));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DetailingCategoryDTO> updateById(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
		var category = service.getReferenceById(id);
		category.updateAttributes(categoryDTO);
		
		return ResponseEntity.ok(new DetailingCategoryDTO(category));
	}
	
	@GetMapping
	public ResponseEntity<List<Category>> listAll(){		
		var list = service.findAll();
		return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){		
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
