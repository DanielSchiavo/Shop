package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.subcategoria.AlterarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaComCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoriaDTO;
import br.com.danielschiavo.shop.services.SubCategoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class SubCategoriaController {
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	@GetMapping("/publico/sub-categoria")
	public ResponseEntity<Page<MostrarSubCategoriaComCategoriaDTO>> listarSubCategorias(Pageable pageable){		
		Page<MostrarSubCategoriaComCategoriaDTO> listaSubCategorias = subCategoriaService.listarSubCategorias(pageable);
		return ResponseEntity.ok(listaSubCategorias);
	}
	
	@PostMapping("/admin/sub-categoria")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<MostrarSubCategoriaDTO> cadastrarSubCategoria(@RequestBody @Valid SubCategoriaDTO dto, UriComponentsBuilder uriBuilder) {
		SubCategoria subCategoria = subCategoriaService.cadastrarSubCategoria(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new MostrarSubCategoriaDTO(subCategoria));
	}
	
	@PutMapping("/admin/sub-categoria/{id}")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<MostrarSubCategoriaDTO> alterarSubCategoriaPorId(@PathVariable Long id, @RequestBody AlterarSubCategoriaDTO categoryDTO) {
		var subCategoriaDTO = subCategoriaService.alterarSubCategoriaPorId(id, categoryDTO);
		
		return ResponseEntity.ok(subCategoriaDTO);
	}
	
	@DeleteMapping("/admin/sub-categoria/{id}")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<?> deletarSubCategoriaPorId(@PathVariable Long id){		
		subCategoriaService.deletarSubCategoriaPorId(id);
		return ResponseEntity.noContent().build();
	}

}