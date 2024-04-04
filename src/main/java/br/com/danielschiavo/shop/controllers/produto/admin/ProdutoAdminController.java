package br.com.danielschiavo.shop.controllers.produto.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.produto.dto.AlterarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.services.produto.admin.ProdutoAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@Tag(name = "Produto", description = "Todos endpoints relacionados com os produtos da loja")
public class ProdutoAdminController {

	@Autowired
	private ProdutoAdminService produtoService;
	
	@DeleteMapping("/admin/produto/{idProduto}")
	@Operation(summary = "Deleta um produto com o id fornecido no parametro da requisição")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable @NotNull Long idProduto) {
		produtoService.deletarProdutoPorId(idProduto);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(path = "/admin/produto")
	@ResponseBody
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cadastra um novo produto")
	public ResponseEntity<MostrarProdutosDTO> cadastrarProduto(
			@RequestBody @Valid CadastrarProdutoDTO cadastrarProdutoDTO,
			UriComponentsBuilder uriBuilder
 			) {
		var mostrarProdutosDTO = produtoService.cadastrarProduto(cadastrarProdutoDTO);
		
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(mostrarProdutosDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(mostrarProdutosDTO);
	}

	@PutMapping("/admin/produto/{idProduto}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera um produto com o id fornecido no parametro da requisição")
	public ResponseEntity<?> alterarProdutoPorId(
			@PathVariable Long idProduto,
			@RequestBody AlterarProdutoDTO alterarProdutoDTO
			) {
		DetalharProdutoDTO detalharProdutoDTO = produtoService.alterarProdutoPorId(idProduto, alterarProdutoDTO);

		return ResponseEntity.ok(detalharProdutoDTO);
	}

}
