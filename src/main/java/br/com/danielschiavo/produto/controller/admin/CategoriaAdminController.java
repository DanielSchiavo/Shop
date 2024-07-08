package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.model.entity.Categoria;
import br.com.danielschiavo.produto.dto.request.CriarCategoriaRequest;
import br.com.danielschiavo.produto.service.CategoriaService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping
@Tag(name = "Categorias - Admin", description = "Todos endpoints relacionados com as categorias dos produtos da loja, para uso exclusivo dos administradores")
public class CategoriaAdminController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@DeleteMapping("/admin/categoria/{idCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta uma categoria e todas subcategorias que tiverem vinculado a essa categoria", operationId = "04_deletarCategoriaPorId")
	public ResponseEntity<?> deletarCategoriaPorId(@PathVariable Long idCategoria) {
		categoriaService.deletarCategoriaPorId(idCategoria);
		return ResponseEntity.ok(Response.success("Categoria deletada com sucesso!", null));
	}
	
	@PostMapping("/admin/categoria")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cria uma categoria", 
	   		   operationId = "03_criarCategoria")
	public ResponseEntity<?> cadastrarCategoria(@RequestBody @Valid CriarCategoriaRequest reques) {
		Categoria categoria = categoriaService.cadastrarCategoria(reques.nome());
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Categoria cadastrada com sucesso!", categoria));
	}
	
	@PutMapping("/admin/categoria/{categoriaId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera o nome da categoria", 
	   		   operationId = "02_alterarNomeCategoriaPorId")
	public ResponseEntity<?> alterarNomeCategoriaPorId(@PathVariable Long categoriaId, @RequestBody @NotNull CriarCategoriaRequest request) {
		Categoria categoria = categoriaService.alterarNomeCategoriaPorId(request, categoriaId);

		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Categoria alterada com sucesso!", null));
	}
}
