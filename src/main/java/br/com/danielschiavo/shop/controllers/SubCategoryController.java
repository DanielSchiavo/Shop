package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.UpdateSubCategoryDTO;
import br.com.danielschiavo.shop.services.SubCategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class SubCategoryController {
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	@GetMapping("/publico/sub-categoria")
	public ResponseEntity<Page<SubCategoria>> listarSubCategorias(Pageable pageable){		
		var list = subCategoriaService.findAll(pageable);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/admin/sub-categoria")
	@Transactional
	public ResponseEntity<MostrarSubCategoriaDTO> cadastrarSubCategoria(@RequestBody @Valid SubCategoriaDTO dto, UriComponentsBuilder uriBuilder) {
		SubCategoria subCategoria = subCategoriaService.cadastrarSubCategoria(dto);
		
		var uri = uriBuilder.path("/shop/admin/sub-categoria/{id}").buildAndExpand(subCategoria.getId()).toUri();
		return ResponseEntity.created(uri).body(new MostrarSubCategoriaDTO(subCategoria));
	}
	
	@PutMapping("/admin/sub-categoria/{id}")
	@Transactional
	public ResponseEntity<MostrarSubCategoriaDTO> alterarSubCategoriaPorId(@PathVariable Long id, @RequestBody UpdateSubCategoryDTO categoryDTO) {
		var subCategoria = subCategoriaService.alterarSubCategoriaPorId(id, categoryDTO);
		
		return ResponseEntity.ok(new MostrarSubCategoriaDTO(subCategoria));
	}
	
	@DeleteMapping("/admin/sub-categoria/{id}")
	public ResponseEntity<?> deletarSubCategoriaPorId(@PathVariable Long id){		
		subCategoriaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
