package br.com.danielschiavo.order.controller;

import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.mapper.OrderMapper;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.service.OrderService;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/orders")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order - Admin", description = "All endpoint related to Order, for administrators use")
public class OrderAdminController {

	@Autowired
	private OrderService service;

	@GetMapping("/customers/{customerId}")
	@Operation(summary = "Get all orders by customer id")
	public ResponseEntity<?> getAllOrdersByCustomerId(@PathVariable Long customerId, Pageable pageable) {
		Page<DetailOrderResponse> pageOrders = service.getAllOrdersByCustomerId(pageable, customerId);

		return ResponseEntity.ok(Response.success("Success recovering all orders of the costumer", pageOrders));
	}

	@GetMapping("/{orderId}")
	@Operation(summary = "Get all data from a specific order by id")
	public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
		DetailOrderResponse order = service.getOrderById(orderId);

		return ResponseEntity.ok(Response.success("Success recovering order", order));
	}

	@GetMapping
	@Operation(summary = "Get all orders realized on store")
	public ResponseEntity<?> getAllOrders(Pageable pageable) {
		Page<DetailOrderResponse> pageOrders = service.getAllOrders(pageable);

		return ResponseEntity.ok(Response.success("Success recovering all orders", pageOrders));
	}
}
