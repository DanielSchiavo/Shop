package br.com.danielschiavo.order.controller;

import br.com.danielschiavo.order.mapper.OrderMapper;
import br.com.danielschiavo.order.dto.request.order.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.order.ShowOrderResponse;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.service.order.OrderService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/orders")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order - User", description = "All endpoints related to customer orders, which the customer can use")
public class OrderUserController {

	@Autowired
	private OrderService service;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private OrderMapper mapper;

	@GetMapping("/{orderId}")
	@Operation(summary = "Get an order by id to get all the details about it")
	public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
		Long customerId = securityService.getCustomerId();

		Order order = service.getOrderByIdAndCustomerId(orderId, customerId);

		return ResponseEntity.ok(Response.success("Success recovering order", mapper.toDto(order)));
	}
	
	@GetMapping
	@Operation(summary = "Get all user orders")
	public ResponseEntity<?> getAllOrders(Pageable pageable) {
		Long customerId = securityService.getCustomerId();

		Page<Order> pageOrder = service.getAllOrdersByCustomerId(pageable, customerId);

		List<ShowOrderResponse> showOrdersList = pageOrder.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
		var resposta = new PageImpl<>(showOrdersList, pageOrder.getPageable(), pageOrder.getTotalElements());
		return ResponseEntity.ok(Response.success("Success recovering all user orders", resposta));
	}
	
	@PostMapping
	@Operation(summary = "Place an order")
	public ResponseEntity<?> realizarPedido(@RequestBody @Valid PlaceOrderRequest request) {
		Long customerId = securityService.getCustomerId();

		Order order = service.placeOrder(customerId, mapper.toEntity(request));

		return ResponseEntity.ok(Response.success("Order placed successfully!", mapper.toDto(order)));
	}
}
