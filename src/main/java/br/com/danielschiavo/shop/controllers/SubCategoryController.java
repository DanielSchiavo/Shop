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

import br.com.danielschiavo.shop.models.subcategory.DetailingSubCategoryDTO;
import br.com.danielschiavo.shop.models.subcategory.SubCategory;
import br.com.danielschiavo.shop.models.subcategory.SubCategoryDTO;
import br.com.danielschiavo.shop.services.SubCategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("sub-category")
public class SubCategoryController {
	
	@Autowired
	private SubCategoryService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DetailingSubCategoryDTO> registerSubCategory(@RequestBody @Valid SubCategoryDTO dto, UriComponentsBuilder uriBuilder) {
		var subCategory = new SubCategory(dto);
		service.save(subCategory);
		
		var uri = uriBuilder.path("/sub-category/{id}").buildAndExpand(subCategory.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DetailingSubCategoryDTO(subCategory));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DetailingSubCategoryDTO> updateById(@PathVariable Long id, @RequestBody @Valid UpdateSubCategoryDTO categoryDTO) {
		var subCategory = service.getReferenceById(id);
		subCategory.updateAttributes(categoryDTO);
		
		return ResponseEntity.ok(new DetailingSubCategoryDTO(subCategory));
	}
	
	@GetMapping
	public ResponseEntity<List<SubCategory>> listAll(){		
		var list = service.findAll();
		return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){		
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
