package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.model.categoria.subcategoria.AlterarSubCategoriaRequest;
import br.com.danielschiavo.produto.model.categoria.subcategoria.CadastrarSubCategoriaRequest;
import br.com.danielschiavo.produto.model.categoria.subcategoria.MostrarSubCategoriaResponse;
import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;
import br.com.danielschiavo.produto.service.admin.SubCategoriaAdminService;
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
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping
@Tag(name = "Sub Categorias - Admin", description = "Todos endpoints relacionados com as subcategorias para uso exclusivo dos administradores")
public class SubCategoriaAdminController {
	
	@Autowired
	private SubCategoriaAdminService subCategoriaAdminService;
	
	@DeleteMapping("/admin/sub-categoria/{idSubCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<?> deletarSubCategoriaPorId(@PathVariable Long idSubCategoria){		
		subCategoriaAdminService.deletarSubCategoriaPorId(idSubCategoria);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/admin/sub-categoria")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cria uma nova subcategoria, uma subcategoria tem que ter uma categoria a qual ela está relacionada")
	public ResponseEntity<?> cadastrarSubCategoria(@RequestBody @Valid CadastrarSubCategoriaRequest dto, UriComponentsBuilder uriBuilder) {
		SubCategoria subCategoria = subCategoriaAdminService.cadastrarSubCategoria(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Sub Categoria cadastrada com sucesso!");
	}
	
	@PutMapping("/admin/sub-categoria/{idSubCategoria}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera o nome de uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<?> alterarSubCategoriaPorId(@PathVariable Long idSubCategoria, @RequestBody AlterarSubCategoriaRequest categoryDTO) {
		SubCategoria subCategoria = subCategoriaAdminService.alterarSubCategoriaPorId(idSubCategoria, categoryDTO);
		
		return ResponseEntity.ok("Sub Categoria alterada com sucesso!");
	}
}
