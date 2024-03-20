package br.com.danielschiavo.shop.controllers;

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

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.services.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Carrinho", description = "Todos endpoints relacionados com o carrinho do usuário")
public class CarrinhoController {
	
	@Autowired
	private CarrinhoService carrinhoService;
	
	@DeleteMapping("/cliente/carrinho/{idProduto}")
	@Operation(summary = "Deleta um produto do carrinho")
	public ResponseEntity<Object> deletarProdutoNoCarrinhoPorIdToken(@PathVariable Long idProduto) {
		carrinhoService.deletarProdutoNoCarrinhoPorIdToken(idProduto);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/cliente/carrinho")
	@Operation(summary = "Pega todos os produtos que estão no carrinho do cliente")
	public ResponseEntity<?> pegarCarrinhoClientePorIdToken() {
		try {
			MostrarCarrinhoClienteDTO mostrarCarrinhoClienteDTO = carrinhoService.pegarCarrinhoClientePorIdToken();
			
			return ResponseEntity.ok(mostrarCarrinhoClienteDTO);
		} catch (ValidacaoException e) {
			return ResponseEntity.badRequest().body(e);
		}
		
	}
	
	@PostMapping("/cliente/carrinho")
	@Operation(summary = "Adiciona um produto no carrinho, se o cliente não tiver um carrinho, também cria automáticamente")
	public ResponseEntity<Object> adicionarProdutosNoCarrinhoPorIdToken(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/cliente/carrinho")
	@Operation(summary = "Seta a quantidade de determinado produto que está no carrinho")
	public ResponseEntity<Object> setarQuantidadeProdutoNoCarrinhoPorIdToken(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.setarQuantidadeProdutoNoCarrinhoPorIdToken(itemCarrinhoDTO);
		return ResponseEntity.ok().build();
	}

}
