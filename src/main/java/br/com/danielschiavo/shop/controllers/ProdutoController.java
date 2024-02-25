package br.com.danielschiavo.shop.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.produto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.services.ProdutoService;

@RestController
@RequestMapping("/shop")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/publico/produto")
	public ResponseEntity<Page<MostrarProdutosDTO>> listarProdutos(Pageable pageable) throws IOException {
		Page<MostrarProdutosDTO> pageableMostrarProdutosDTO = produtoService.listarProdutos(pageable);
		
		return ResponseEntity.ok(pageableMostrarProdutosDTO);
	}
	
	@GetMapping("/publico/produto/{id}")
	public ResponseEntity<DetalharProdutoDTO> detalharProdutoPorId(@PathVariable Long id) {
		DetalharProdutoDTO detalharProdutoDTO = produtoService.detalharProdutoPorId(id);
		
		return ResponseEntity.ok(detalharProdutoDTO);
	}
	
	@PostMapping(path = "/admin/produto" , consumes = "multipart/form-data")
	@Transactional
	@ResponseBody
	public ResponseEntity<MostrarProdutosDTO> cadastrarProduto(
			@RequestParam(name = "produto") String jsonProduto,
			@RequestPart(name = "arquivos") MultipartFile[] multipartArquivos,
			@RequestParam(name = "posicoes") String stringPosicoes,
			UriComponentsBuilder uriBuilder
 			) {
		var mostrarProdutosDTO = produtoService.cadastrarProduto(jsonProduto, multipartArquivos, stringPosicoes);
		
		var uri = uriBuilder.path("/products/{id}").buildAndExpand(mostrarProdutosDTO.id()).toUri();
		return ResponseEntity.created(uri).body(mostrarProdutosDTO);

	}

	@PutMapping("/admin/produto/{id}")
	@Transactional
	public ResponseEntity<?> alterarProdutoPorId(
			@PathVariable Long id,
			@RequestPart(name = "produto", required = false) String jsonProduto,
			@RequestPart(name = "arquivos", required = false) MultipartFile[] multipartArquivos,
			@RequestPart(name = "posicoes", required = false) String stringPosicoes
			) {
		Produto produto = produtoService.alterarProdutoPorId(id, jsonProduto, multipartArquivos, stringPosicoes);

		return ResponseEntity.ok("Atualizado com sucesso! ");
	}

	@DeleteMapping("/admin/produto/{id}")
	@Transactional
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable Long id) {
		produtoService.deletarProdutoPorId(id);
		return ResponseEntity.noContent().build();
	}
}
