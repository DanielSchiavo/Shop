package br.com.danielschiavo.produto.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.produto.dto.response.MostrarProdutosResponse;
import br.com.danielschiavo.produto.mapper.ProdutoMapper;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.service.produto.ProdutoService;
import br.com.danielschiavo.produto.service.user.ProdutoUserService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
	private ProdutoService produtoService;

	@Autowired
	private ProdutoMapper mapper;
	
	@GetMapping("/publico/produto")
	@Operation(summary = "Lista todos os produtos da loja")
	public ResponseEntity<?> listarProdutos(Pageable pageable) {
		Page<Produto> pageProdutos = produtoService.listarProdutos(pageable);

		List<MostrarProdutosResponse> listaMostrarProdutos = pageProdutos.getContent().stream()
				.map(mapper::toMostrarProdutosDto).collect(Collectors.toList());

		var resposta = new PageImpl<>(listaMostrarProdutos, pageable, pageProdutos.getTotalElements());

		return ResponseEntity.ok(Response.success("Sucesso ao recuperar produtos da loja", resposta));
	}
	
	@GetMapping("/publico/produto/{produtoId}")
	@Operation(summary = "Pega todos os dados do produto com id fornecido no parametro da requisição")
	public ResponseEntity<?> detalharProdutoPorId(@PathVariable Long produtoId) {
		Produto produto = produtoService.pegarProdutoPorId(produtoId);
		
		return ResponseEntity.ok(Response.success("Sucesso ao recuperar o produto", mapper.toDetalharProdutoDto(produto)));
	}

}
