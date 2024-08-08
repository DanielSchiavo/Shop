package br.com.danielschiavo.sales.controller;

import br.com.danielschiavo.catalog.dto.response.product.ShowProductsResponse;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.sales.dto.response.ShowCartItemResponse;
import br.com.danielschiavo.sales.dto.response.ShowCartResponse;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.sales.dto.request.AddCartItemRequest;
import br.com.danielschiavo.sales.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/user/carts")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Cart", description = "All endpoints related to the customer's cart, which they can use")
public class CartController {

	@Autowired
	private CartService service;

	@Autowired
	private ProductService productService;

	@Autowired
	private SecurityService securityService;

	@DeleteMapping("/products/{productsId}")
	@Operation(summary = "Deletes one or more products from the cart")
	public ResponseEntity<?> removeProductFromCart(@PathVariable Long productsId) {
		Long customerId = securityService.getCustomerId();
		service.removeProductFromCart(customerId, productsId);
		return ResponseEntity.ok().body(Response.success("Removal successfully completed!", null));
	}

	@GetMapping
	@Operation(summary = "Gets all products that are in the customer's cart")
	public ResponseEntity<?> getCustomerCartByIdToken() {
		Long customerId = securityService.getCustomerId();
		List<ShowCartItemResponse> cartItems = service.getCartItems(customerId);
		List<ShowProductsResponse> products = productService.getProductsById(cartItems.stream().map(ShowCartItemResponse::productId).toList());
		ShowCartResponse cart = service.updateCart(customerId, products);

		return ResponseEntity.ok(Response.success("Successfully retrieved cart products", cart));
	}

	@PostMapping
	@Operation(summary = "Adds a product to the cart, if the customer does not have a cart, it also creates one automatically")
	public ResponseEntity<?> addProductToCart(@RequestBody @Valid AddCartItemRequest request) {
		Long customerId = securityService.getCustomerId();
		productService.checkIfProductExist(request.productId());
		service.addProductToCart(customerId, request);
		return ResponseEntity.ok().body(Response.success("Product added to cart!", null));
	}
}