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
	
	@GetMapping("/cliente/carrinho")
	@Operation(summary = "Pega todos os produtos que estão no carrinho do cliente", 
			   operationId = "01_pegarCarrinhoCliente")
	public ResponseEntity<MostrarCarrinhoClienteDTO> pegarCarrinhoCliente() {
		MostrarCarrinhoClienteDTO mostrarCarrinhoClienteDTO = carrinhoService.pegarCarrinhoCliente();
		
		return ResponseEntity.ok(mostrarCarrinhoClienteDTO);
	}
	
	@PostMapping("/cliente/carrinho")
	@Operation(summary = "Adiciona um produto no carrinho, se o cliente não tiver um carrinho, também cria automáticamente", 
			   operationId = "02_AdicionarProdutoNoCarrinho")
	public ResponseEntity<Object> adicionarProdutosNoCarrinho(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.adicionarProdutosNoCarrinho(itemCarrinhoDTO);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/cliente/carrinho")
	@Operation(summary = "Seta a quantidade de determinado produto que está no carrinho", 
	   		   operationId = "03_setarQuantidadeProdutoNoCarrinho")
	public ResponseEntity<Object> setarQuantidadeProdutoNoCarrinho(@RequestBody @Valid ItemCarrinhoDTO itemCarrinhoDTO) {
		carrinhoService.setarQuantidadeProdutoNoCarrinho(itemCarrinhoDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/cliente/carrinho/{idProduto}")
	@Operation(summary = "Deleta um produto do carrinho", 
	   		   operationId = "04_deletarProdutoNoCarrinho")
	public ResponseEntity<Object> deletarProdutoNoCarrinho(@PathVariable Long idProduto) {
		carrinhoService.deletarProdutoNoCarrinho(idProduto);
		return ResponseEntity.noContent().build();
	}

}
