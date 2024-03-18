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
import br.com.danielschiavo.shop.models.subcategoria.CadastrarSubCategoriaDTO;
import br.com.danielschiavo.shop.services.SubCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@Tag(name = "Sub Categorias", description = "Todos endpoints relacionados com as subcategorias, uma subcategoria é dependente de uma categoria")
public class SubCategoriaController {
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	@GetMapping("/publico/sub-categoria")
	@Operation(summary = "Lista todas as subcategorias existentes")
	public ResponseEntity<Page<MostrarSubCategoriaComCategoriaDTO>> listarSubCategorias(Pageable pageable){		
		Page<MostrarSubCategoriaComCategoriaDTO> listaSubCategorias = subCategoriaService.listarSubCategorias(pageable);
		return ResponseEntity.ok(listaSubCategorias);
	}
	
	
//	------------------------------
//	------------------------------
//	ENDPOINTS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	@DeleteMapping("/admin/sub-categoria/{idSubCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<?> deletarSubCategoriaPorId(@PathVariable Long idSubCategoria){		
		subCategoriaService.deletarSubCategoriaPorId(idSubCategoria);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/admin/sub-categoria")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cria uma nova subcategoria, uma subcategoria tem que ter uma categoria a qual ela está relacionada")
	public ResponseEntity<MostrarSubCategoriaDTO> cadastrarSubCategoria(@RequestBody @Valid CadastrarSubCategoriaDTO dto, UriComponentsBuilder uriBuilder) {
		MostrarSubCategoriaDTO subCategoria = subCategoriaService.cadastrarSubCategoria(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(subCategoria);
	}
	
	@PutMapping("/admin/sub-categoria/{idSubCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera o nome de uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<MostrarSubCategoriaDTO> alterarSubCategoriaPorId(@PathVariable Long idSubCategoria, @RequestBody AlterarSubCategoriaDTO categoryDTO) {
		var subCategoriaDTO = subCategoriaService.alterarSubCategoriaPorId(idSubCategoria, categoryDTO);
		
		return ResponseEntity.ok(subCategoriaDTO);
	}
}
