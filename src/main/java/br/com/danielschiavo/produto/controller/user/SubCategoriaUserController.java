package br.com.danielschiavo.produto.controller.user;

import br.com.danielschiavo.produto.model.categoria.subcategoria.MostrarSubCategoriaComCategoriaResponse;
import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;
import br.com.danielschiavo.produto.service.user.SubCategoriaUserService;
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
@Tag(name = "Sub Categorias - User", description = "Todos endpoints relacionados com as subcategorias, de uso publico")
public class SubCategoriaUserController {
	
	@Autowired
	private SubCategoriaUserService subCategoriaService;
	
	@GetMapping("/publico/sub-categoria")
	@Operation(summary = "Lista todas as subcategorias existentes")
	public ResponseEntity<?> listarSubCategorias(Pageable pageable){
		Page<SubCategoria> listaSubCategorias = subCategoriaService.listarSubCategorias(pageable);
		return ResponseEntity.ok(listaSubCategorias);
	}
	
}
