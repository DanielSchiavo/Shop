package br.com.danielschiavo.customer.controller.user;


import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.dto.response.card.ShowCardResponse;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/customers/cards")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Card", description = "All Card related endpoints for the Customer")
public class CardController {

	@Autowired
	private CardService service;

	@Autowired
	private SecurityService securityService;

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
		List<ShowCardResponse> response = service.getAllCardsByCustomerId(customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering all cards", response));
	}
	
	@GetMapping("/{cardId}")
	@Operation(summary = "Get card details with the provided id")
	public ResponseEntity<?> getAllCardsByCustomerId(@PathVariable Long cardId) {
		Long customerId = securityService.getCustomerId();
		DetailCardResponse response = service.getCardByIdAndCustomerId(cardId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering card", response));
	}
	
	@PostMapping
	@Operation(summary = "Register a new Card to Customer")
	public ResponseEntity<?> registerCard(@RequestBody @Valid RegisterCardRequest request) {
		Long customerId = securityService.getCustomerId();
		DetailCardResponse response = service.registerCard(customerId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Success registering card", response));
	}
	
	@PatchMapping("/{cardId}")
	@Operation(summary = "Endpoint to Switch Default Card Status",
			description = """
					This endpoint allows you to switch a card's status between default and non-default based on ID. 
					If the card is not currently the default, it will be set as default; if it is already the default, it will be changed to non-default.
					""")
	public ResponseEntity<?> switchDefaultCardStatus(@PathVariable Long cardId) {
		Long customerId = securityService.getCustomerId();
		service.switchIsDefaultStatus(cardId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Card updated successfully!", null));
	}
	
	

}
