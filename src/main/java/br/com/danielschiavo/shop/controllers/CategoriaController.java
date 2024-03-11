package br.com.danielschiavo.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.CategoriaDTO;
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;
import br.com.danielschiavo.shop.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@Tag(name = "Categorias", description = "Todos endpoints relacionados com as categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping("/publico/categoria")
	@Operation(summary = "Lista todas as categorias existentes", 
	   		   operationId = "01_listarCategorias")
	public ResponseEntity<Page<Categoria>> listarCategorias(Pageable pageable){		
		var lista = categoriaRepository.findAll(pageable);
		return ResponseEntity.ok(lista);
	}
	
	@PutMapping("/admin/categoria/{idCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera o nome da categoria", 
	   		   operationId = "02_alterarNomeCategoriaPorId")
	public ResponseEntity<?> alterarNomeCategoriaPorId(@PathVariable Long idCategoria, @RequestBody @NotNull CategoriaDTO categoriaDTO) {
		MostrarCategoriaDTO mostrarCategoriaDTO = categoriaService.alterarNomeCategoriaPorId(idCategoria, categoriaDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(mostrarCategoriaDTO);
	}
	
	@PostMapping("/admin/categoria")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cria uma categoria", 
	   		   operationId = "03_criarCategoria")
	public ResponseEntity<?> criarCategoria(@RequestBody @Valid CategoriaDTO categoriaDTO) {
		try {
			MostrarCategoriaDTO mostrarCategoriaDTO = categoriaService.criarCategoria(categoriaDTO.nome());
			return ResponseEntity.status(HttpStatus.CREATED).body(mostrarCategoriaDTO);
		} catch (ValidacaoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		}
		
	}
	
	@DeleteMapping("/admin/categoria/{idCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta uma categoria e todas subcategorias que tiverem vinculado a essa categoria", operationId = "04_deletarCategoriaPorId")
	public ResponseEntity<?> deletarCategoriaPorId(@PathVariable Long idCategoria) {
	    try {
	        categoriaService.deletarCategoriaPorId(idCategoria);
	        return ResponseEntity.noContent().build();
	    } catch (DataIntegrityViolationException e) {
	        return ResponseEntity.badRequest().body("Não é possível excluir esta categoria enquanto houver produtos relacionados. Por favor, remova o relacionamento de produto com categoria primeiro.");
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().body("Erro ao deletar categoria.");
	    }
	}

}
