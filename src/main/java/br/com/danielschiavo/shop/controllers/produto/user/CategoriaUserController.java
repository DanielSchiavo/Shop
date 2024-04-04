package br.com.danielschiavo.shop.controllers.produto.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.services.produto.user.CategoriaUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/shop")
@Tag(name = "Categorias", description = "Todos endpoints relacionados com as categorias")
public class CategoriaUserController {
	
	@Autowired
	private CategoriaUserService categoriaService;
	
	@GetMapping("/publico/categoria")
	@Operation(summary = "Lista todas as categorias existentes")
	public ResponseEntity<?> listarCategorias(Pageable pageable){		
		Page<?> lista = categoriaService.listarCategorias(pageable);
		return ResponseEntity.ok(lista);
	}
}
