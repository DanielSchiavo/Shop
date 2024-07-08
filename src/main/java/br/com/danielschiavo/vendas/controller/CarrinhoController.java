package br.com.danielschiavo.vendas.controller;

import java.util.List;

import br.com.danielschiavo.vendas.dto.response.MostrarCarrinhoClienteResponse;
import br.com.danielschiavo.vendas.dto.RemoverProdutoDoCarrinhoDTO;
import br.com.danielschiavo.vendas.dto.request.AdicionarItemCarrinhoRequest;
import br.com.danielschiavo.vendas.service.CarrinhoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - Carrinho", description = "Todos endpoints relacionados com o carrinho do cliente, que o próprio poderá utilizar")
public class CarrinhoController {
	
	@Autowired
	private CarrinhoUserService carrinhoService;
	
	@DeleteMapping("/cliente/carrinho/{produtosId}")
	@Operation(summary = "Deleta um produto do carrinho")
	public ResponseEntity<?> deletarProdutoNoCarrinhoPorIdToken(@PathVariable List<Long> produtosId, HttpServletRequest request) {
		List<RemoverProdutoDoCarrinhoDTO> respostaDeletarProdutoNoCarrinho = carrinhoService.deletarProdutoNoCarrinhoPorIdToken(produtosId);
		return ResponseEntity.ok().body(respostaDeletarProdutoNoCarrinho);
	}
	
	@GetMapping("/cliente/carrinho")
	@Operation(summary = "Pega todos os produtos que estão no carrinho do cliente")
	public ResponseEntity<?> pegarCarrinhoClientePorIdToken(HttpServletRequest request) {
		MostrarCarrinhoClienteResponse mostrarCarrinhoClienteDTO = carrinhoService.pegarCarrinhoClientePorIdToken();
			
		return ResponseEntity.ok(mostrarCarrinhoClienteDTO);
	}
	
	@PostMapping("/cliente/carrinho")
	@Operation(summary = "Adiciona um produto no carrinho, se o cliente não tiver um carrinho, também cria automáticamente")
	public ResponseEntity<Object> adicionarProdutosNoCarrinhoPorIdToken(@RequestBody @Valid AdicionarItemCarrinhoRequest itemCarrinhoDTO, HttpServletRequest request) {
		String respostaAdicionarProdutoNoCarrinho = carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(itemCarrinhoDTO);
		return ResponseEntity.ok().body(respostaAdicionarProdutoNoCarrinho);
	}
	
	@PutMapping("/cliente/carrinho")
	@Operation(summary = "Seta a quantidade de determinado produto que está no carrinho")
	public ResponseEntity<Object> setarQuantidadeProdutoNoCarrinhoPorIdToken(@RequestBody @Valid AdicionarItemCarrinhoRequest itemCarrinhoDTO, HttpServletRequest request) {
		carrinhoService.setarQuantidadeProdutoNoCarrinhoPorIdToken(itemCarrinhoDTO);
		return ResponseEntity.ok().build();
	}
}
