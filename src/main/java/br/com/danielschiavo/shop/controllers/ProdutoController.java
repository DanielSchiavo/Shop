package br.com.danielschiavo.shop.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.produto.AlterarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/shop")
@Tag(name = "Produto", description = "Todos endpoints relacionados com os produtos da loja")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/publico/produto")
	@Operation(summary = "Lista todos os produtos da loja")
	public ResponseEntity<Page<MostrarProdutosDTO>> listarProdutos(Pageable pageable) throws IOException {
		Page<MostrarProdutosDTO> pageableMostrarProdutosDTO = produtoService.listarProdutos(pageable);
		
		return ResponseEntity.ok(pageableMostrarProdutosDTO);
	}
	
	@GetMapping("/publico/produto/{idProduto}")
	@Operation(summary = "Pega todos os dados do produto com id fornecido no parametro da requisição")
	public ResponseEntity<DetalharProdutoDTO> detalharProdutoPorId(@PathVariable Long idProduto) {
		DetalharProdutoDTO detalharProdutoDTO = produtoService.detalharProdutoPorId(idProduto);
		
		return ResponseEntity.ok(detalharProdutoDTO);
	}
	
	@PostMapping(path = "/admin/produto")
	@ResponseBody
	@SecurityRequirement(name = "bearer-key")
	@Operation(summary = "Cadastra um novo produto")
	public ResponseEntity<MostrarProdutosDTO> cadastrarProduto(
			@RequestBody CadastrarProdutoDTO cadastrarProdutoDTO,
			UriComponentsBuilder uriBuilder
 			) {
		var mostrarProdutosDTO = produtoService.cadastrarProduto(cadastrarProdutoDTO);
		
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(mostrarProdutosDTO.id()).toUri();
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

	@DeleteMapping("/admin/produto/{idProduto}")
	@Operation(summary = "Deleta um produto com o id fornecido no parametro da requisição")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable Long idProduto) {
		produtoService.deletarProdutoPorId(idProduto);
		return ResponseEntity.noContent().build();
	}
}
