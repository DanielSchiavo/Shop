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

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.CategoriaDTO;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;
import br.com.danielschiavo.shop.services.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping("/publico/categoria")
	public ResponseEntity<Page<Categoria>> listarCategorias(Pageable pageable){		
		var lista = categoriaRepository.findAll(pageable);
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping("/admin/categoria")
	@Transactional
	public ResponseEntity<?> cadastrarCategoria(@RequestBody @Valid CategoriaDTO categoriaDTO) {
		categoriaService.cadastrarCategoria(categoriaDTO.nome());
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/admin/categoria/{id}")
	@Transactional
	public ResponseEntity<?> atualizarNomePorId(@PathVariable Long id, @RequestBody @NotNull String nome) {
		categoriaService.atualizarNomePorId(id, nome);
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/admin/categoria/{id}")
	@Transactional
	public ResponseEntity<?> deleteById(@PathVariable Long id){		
		categoriaRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
