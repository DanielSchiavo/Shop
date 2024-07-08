package br.com.danielschiavo.produto.controller.user;

import br.com.danielschiavo.produto.model.entity.Categoria;
import br.com.danielschiavo.produto.service.CategoriaService;
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
@RequestMapping
@Tag(name = "Categorias - User", description = "Todos endpoints relacionados com as categorias, de uso publico")
public class CategoriaUserController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping("/publico/categoria")
	@Operation(summary = "Lista todas as categorias existentes")
	public ResponseEntity<?> listarCategorias(Pageable pageable){		
		Page<Categoria> pageCategorias = categoriaService.listarCategorias(pageable);
		return ResponseEntity.ok(Response.success("Sucesso ao recuperar as categorias", pageCategorias));
	}
}
