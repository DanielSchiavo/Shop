package br.com.danielschiavo.vendas.controller;

import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.vendas.dto.request.ItemCarrinhoRequest;
import br.com.danielschiavo.vendas.mapper.CarrinhoMapper;
import br.com.danielschiavo.vendas.model.entity.Carrinho;
import br.com.danielschiavo.vendas.model.entity.ItemCarrinho;
import br.com.danielschiavo.vendas.service.CarrinhoService;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/carrinhos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Carrinho", description = "Todos endpoints relacionados com o carrinho do customer, que o próprio poderá utilizar")
public class CarrinhoController {
	
	@Autowired
	private CarrinhoService carrinhoService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CarrinhoMapper mapper;
	
	@DeleteMapping("/produtos/{produtosId}")
	@Operation(summary = "Deleta um ou vários produtos do carrinho")
	public ResponseEntity<?> removerProdutoDoCarrinho(@PathVariable Long[] produtosId) {
		Long clienteId = securityService.getCustomerId();
		carrinhoService.removerProdutoDoCarrinho(clienteId, produtosId);
		return ResponseEntity.ok().body(Response.success("Remoção realizada com sucesso!", null));
	}
	
	@GetMapping("/cliente/carrinho")
	@Operation(summary = "Pega todos os produtos que estão no carrinho do customer")
	public ResponseEntity<?> pegarCarrinhoClientePorIdToken() {
		Long clienteId = securityService.getCustomerId();
		Carrinho carrinho = carrinhoService.pegarCarrinhoPorClienteId(clienteId);
			
		return ResponseEntity.ok(Response.success("Sucesso ao recuperar produtos do carrinho", mapper.toDto(carrinho)));
	}
	
	@PostMapping("/cliente/carrinho")
	@Operation(summary = "Adiciona um produto no carrinho, se o customer não tiver um carrinho, também cria automáticamente")
	public ResponseEntity<?> adicionarProdutosNoCarrinhoPorIdToken(@RequestBody @Valid ItemCarrinhoRequest request) {
		Long clienteId = securityService.getCustomerId();
		ItemCarrinho adicionarItem = mapper.toEntity(request);
		Carrinho carrinho = carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(clienteId, adicionarItem);
		return ResponseEntity.ok().body(Response.success("Produto adicionado ao carrinho!", null));
	}
	
	@PutMapping("/cliente/carrinho")
	@Operation(summary = "Seta a quantidade de determinado produto que está no carrinho")
	public ResponseEntity<?> setarQuantidadeProdutoNoCarrinhoPorIdToken(@RequestBody @Valid ItemCarrinhoRequest request) {
		Long clienteId = securityService.getCustomerId();
		ItemCarrinho setarItem = mapper.toEntity(request);
		carrinhoService.setarQuantidadeProdutoNoCarrinho(clienteId, setarItem);
		return ResponseEntity.ok(Response.success("Alterado com sucesso!", null));
	}
}
