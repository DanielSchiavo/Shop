package br.com.danielschiavo.produto.controller.admin;

import br.com.danielschiavo.produto.dto.request.AlterarProdutoRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.mapper.ProdutoMapper;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.service.produto.ProdutoService;
import br.com.danielschiavo.shared.Response;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/produtos")
@Tag(name = "Produto - Admin", description = "Todos endpoints relacionados com os produtos da loja, para uso exclusivo dos administradores")
public class ProdutoAdminController {

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoMapper mapper;

	@DeleteMapping("/{produtoId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Deleta um produto com o id fornecido no parametro da requisição")
	public ResponseEntity<?> deletarProduto(@PathVariable @NotNull Long produtoId) {
		produtoService.deletarProdutoPorId(produtoId);
		return ResponseEntity.ok(Response.success("Produto deletado com sucesso!", null));
	}

	@PostMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cadastra um novo produto")
	public ResponseEntity<?> cadastrarProduto(
			@RequestBody @Valid CadastrarProdutoRequest request,
			UriComponentsBuilder uriBuilder) {
		Produto cadastrarProduto = mapper.toEntity(request);
		Produto produto = produtoService.cadastrarProduto(cadastrarProduto);
		var uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(Response.success("Produto cadastrado com sucesso!", null));
	}

	@PutMapping("/{produtoId}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Altera um produto com o id fornecido no parametro da requisição")
	public ResponseEntity<?> alterarProdutoPorId(
			@PathVariable Long produtoId,
			@RequestBody AlterarProdutoRequest request) {
		Produto produtoAtualizado = mapper.toEntity(request);
		Produto produto = produtoService.alterarProdutoPorId(produtoId, produtoAtualizado);
			
		return ResponseEntity.ok(Response.success("Produto alterado com sucesso!", null));
	}

}
