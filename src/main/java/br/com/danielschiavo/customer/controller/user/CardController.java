package br.com.danielschiavo.customer.controller.user;


import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.mapper.CardMapper;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.service.cartao.CardService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/user/clientes/cartoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Cartão", description = "Todos endpoints relacionados com os cartões do customer, que o próprio poderá utilizar")
public class CardController {

	@Autowired
	private CardService service;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CardMapper mapper;
	
	@DeleteMapping("/{cardId}")
	@Operation(summary = "Delete the card of the logged in customer with the given id")
	public ResponseEntity<?> deleteCardById(@PathVariable Long cardId) {
		Long customerId = securityService.getCustomerId();
		service.deleteCardById(cardId, customerId);
		return ResponseEntity.ok(Response.success("Card deleted successfully!", null));
	}

	@GetMapping
	@Operation(summary = "Get all cards from the logged in customer")
	public ResponseEntity<?> getAllCardsByCustomerId() {
		Long customerId = securityService.getCustomerId();
		List<Card> cardList = service.getAllCardsByCustomerId(customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering all cards", cardList));
	}
	
	@GetMapping("/{cardId}")
	@Operation(summary = "Get card details with the provided id")
	public ResponseEntity<?> getAllCardsByCustomerId(@PathVariable Long cardId) {
		Long customerId = securityService.getCustomerId();
		Card card = service.getCardByIdAndCustomerId(cardId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering card", mapper.toDto(card)));
	}
	
	@PostMapping
	@Operation(summary = "Cadastra um novo cartão para o usuário")
	public ResponseEntity<?> registerCard(@RequestBody @Valid RegisterCardRequest request) {
		Long customerId = securityService.getCustomerId();
		Card card = mapper.toEntity(request, customerId);
		Card registredCard = service.registerCard(customerId, card);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Success registering card", mapper.toDto(registredCard)));
	}
	
	@PutMapping("/{cardId}")
	@Operation(summary = "Endpoint to Switch Default Card Status",
			description = """
					This endpoint allows you to switch a card's status between default and non-default based on ID. 
					If the card is not currently the default, it will be set as default; if it is already the default, it will be changed to non-default.
					""")
	public ResponseEntity<?> switchDefaultCardStatus(@PathVariable Long cardId) {
		Long customerId = securityService.getCustomerId();
		service.switchDefaultCardStatus(cardId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Cartão alterado com sucesso!", null));
	}
	
	

}
