package br.com.danielschiavo.catalog.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.service.product.ProductService;
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
@Tag(name = "Product - User", description = "All endpoints related to a Product, for public use")
public class ProductUserController {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductMapper mapper;
	
	@GetMapping("/publico/produto")
	@Operation(summary = "Get all products")
	public ResponseEntity<?> listarProdutos(Pageable pageable) {
		Page<Product> pageProdutos = service.getAllProducts(pageable);

		List<ShowProductsResponse> listaMostrarProdutos = pageProdutos.getContent().stream()
				.map(mapper::toShowProducts).collect(Collectors.toList());

		var resposta = new PageImpl<>(listaMostrarProdutos, pageable, pageProdutos.getTotalElements());

		return ResponseEntity.ok(Response.success("Sucesso ao recuperar products da loja", resposta));
	}
	
	@GetMapping("/publico/produto/{produtoId}")
	@Operation(summary = "Pega todos os dados do produto com id fornecido no parametro da requisição")
	public ResponseEntity<?> detalharProdutoPorId(@PathVariable Long produtoId) {
		Product produto = service.getProductById(produtoId);
		
		return ResponseEntity.ok(Response.success("Sucesso ao recuperar o produto", mapper.toDetailProduct(produto)));
	}

}
