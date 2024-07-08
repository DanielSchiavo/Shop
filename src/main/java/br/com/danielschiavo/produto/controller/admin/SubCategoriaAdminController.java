package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.dto.request.AlterarSubCategoriaRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarSubCategoriaRequest;
import br.com.danielschiavo.produto.model.entity.SubCategoria;
import br.com.danielschiavo.produto.service.SubCategoriaService;
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
	private SubCategoriaService subCategoriaService;
	
	@DeleteMapping("/admin/sub-categoria/{subCategoriaId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<?> deletarSubCategoriaPorId(@PathVariable Long subCategoriaId){
		subCategoriaService.deletarSubCategoriaPorId(subCategoriaId);
		return ResponseEntity.ok(Response.success("Sub Categoria deletada com sucesso!", null));
	}
	
	@PostMapping("/admin/sub-categoria")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cria uma nova subcategoria, uma subcategoria tem que ter uma categoria a qual ela está relacionada")
	public ResponseEntity<?> cadastrarSubCategoria(@RequestBody @Valid CadastrarSubCategoriaRequest dto, UriComponentsBuilder uriBuilder) {
		SubCategoria subCategoria = subCategoriaService.cadastrarSubCategoria(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Sub Categoria cadastrada com sucesso!", subCategoria));
	}
	
	@PutMapping("/admin/sub-categoria/{subCategoriaId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera o nome de uma subcategoria com o id fornecido no parametro da requisição")
	public ResponseEntity<?> alterarSubCategoriaPorId(@PathVariable Long subCategoriaId, @RequestBody AlterarSubCategoriaRequest request) {
		SubCategoria subCategoria = subCategoriaService.alterarSubCategoriaPorId(request, subCategoriaId);
		
		return ResponseEntity.ok(Response.success("Sub Categoria alterada com sucesso!", null));
	}
}
