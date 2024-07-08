package br.com.danielschiavo.produto.controller.user;

import java.io.IOException;
import java.util.List;

import br.com.danielschiavo.produto.model.DetalharProdutoResponse;
import br.com.danielschiavo.produto.model.MostrarProdutosResponse;
import br.com.danielschiavo.produto.service.user.ProdutoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Produto - User", description = "Todos endpoints relacionados com os produtos da loja, de uso publico")
public class ProdutoUserController {

	@Autowired
	private ProdutoUserService produtoService;
	
	@GetMapping("/publico/produto")
	@Operation(summary = "Lista todos os produtos da loja")
	public ResponseEntity<?> listarProdutos(Pageable pageable) throws IOException {
		Page<MostrarProdutosResponse> pageableMostrarProdutosDTO = produtoService.listarProdutos(pageable);
		
		return ResponseEntity.ok(pageableMostrarProdutosDTO);
	}
	
	@GetMapping("/publico/produto/{produtoId}")
	@Operation(summary = "Pega todos os dados do produto com id fornecido no parametro da requisição")
	public ResponseEntity<?> detalharProdutoPorId(@PathVariable Long produtoId) {
		DetalharProdutoResponse detalharProdutoDTO = produtoService.detalharProdutoPorId(produtoId);
		
		return ResponseEntity.ok(detalharProdutoDTO);
	}

}
