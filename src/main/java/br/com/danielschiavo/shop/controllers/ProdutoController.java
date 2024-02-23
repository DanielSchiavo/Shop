package br.com.danielschiavo.shop.controllers;

import java.io.IOException;
import java.util.List;

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
import br.com.danielschiavo.shop.models.produto.MostrarArquivosProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.ProdutoDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.services.ProdutoService;
import br.com.danielschiavo.shop.services.SubCategoriaService;

@RestController
@RequestMapping("/shop")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	private final int MAX_FILES = 10;
	
	@GetMapping("/publico/produto")
	public ResponseEntity<Page<MostrarProdutosDTO>> listarProdutos(Pageable pageable) throws IOException {
		Page<MostrarProdutosDTO> pageableMostrarProdutosDTO = produtoService.listarProdutos(pageable);
		return ResponseEntity.ok(pageableMostrarProdutosDTO);
	}
	
	@GetMapping("/publico/produto/{id}")
	public ResponseEntity<DetalharProdutoDTO> detalharProdutoPorId(@PathVariable Long id) throws IOException {
		Produto produto = produtoService.getReferenceById(id);
		List<MostrarArquivosProdutoDTO> mostrarArquivosProdutoDTO = produtoService.carregarArquivosProduto(produto.getArquivosProduto());
		DetalharProdutoDTO detalharProdutoDTO = new DetalharProdutoDTO(produto, mostrarArquivosProdutoDTO);
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
		ProdutoDTO produtoDTO = produtoService.transformarStringJsonEmProdutoDTO(jsonProduto);
		int[] posicoes = produtoService.transformarStringPosicaoEmArrayInt(stringPosicoes);
		
		if (multipartArquivos.length != posicoes.length) {
			ResponseEntity.badRequest().body("Cada FILE deve corresponder a uma POSITION!");
		}
		
		if (posicoes[0] != 0) {
			throw new RuntimeException("O produto precisa ter uma imagem principal! que corresponde a POSITION: 0");
		}
		
		if (multipartArquivos.length > MAX_FILES && posicoes.length > MAX_FILES) {
			throw new RuntimeException("O número máximo de imagens e vídeos é " + MAX_FILES + "!");
		}
		
		SubCategoria subCategoria = subCategoriaService.verificarId(produtoDTO.subCategoriaId());
		
		Produto produto = new Produto(produtoDTO, subCategoria);
		MostrarProdutosDTO mostrarProdutosDTO = null;
		try {
			mostrarProdutosDTO = produtoService.criarNovoProduto(produto, multipartArquivos, posicoes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		Produto produto = produtoService.getReferenceById(id);
		int[] posicoes = produtoService.transformarStringPosicaoEmArrayInt(stringPosicoes);

		if (jsonProduto == null && multipartArquivos == null && posicoes == null) {
			ResponseEntity.badRequest().body("Nenhum dado foi enviado com a requisição, portanto nada foi alterado.");
		}
		
		if (multipartArquivos.length != posicoes.length) {
			ResponseEntity.badRequest().body("Cada ARQUIVO deve corresponder a uma POSICAO, para saber a ordem de exibicao do arquivo.");
		}
		
		produtoService.atualizarDadosProduto(produto, jsonProduto, multipartArquivos, stringPosicoes);

		return ResponseEntity.ok("Atualizado com sucesso! ");
	}

	@DeleteMapping("/admin/produto/{id}")
	@Transactional
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable Long id) {
		produtoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
