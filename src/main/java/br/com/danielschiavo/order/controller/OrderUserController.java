package br.com.danielschiavo.order.controller;

import br.com.danielschiavo.catalog.dto.response.DetailProductFileResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.customer.service.address.AddressService;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.customer.service.customer.CustomerService;
import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.delivery.service.DeliveryService;
import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.order.dto.request.OrderItemRequest;
import br.com.danielschiavo.order.dto.request.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.service.OrderService;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import br.com.danielschiavo.payment.service.PaymentService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
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
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CardService cardService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private StorageProperties storageProperties;

	@Autowired
	private FileReferenceService fileService;

	@GetMapping("/{orderId}")
	@Operation(summary = "Get an order by id to get all the details about it")
	public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
		Long customerId = securityService.getCustomerId();

		DetailOrderResponse response = service.getOrderByIdAndCustomerId(orderId, customerId);

		return ResponseEntity.ok(Response.success("Success recovering order", response));
	}
	
	@GetMapping
	@Operation(summary = "Get all user orders")
	public ResponseEntity<?> getAllOrders(Pageable pageable) {
		Long customerId = securityService.getCustomerId();

		Page<DetailOrderResponse> response = service.getAllOrdersByCustomerId(pageable, customerId);

		return ResponseEntity.ok(Response.success("Success recovering all user orders", response));
	}
	
	@PostMapping
	@Operation(summary = "Place an order")
	public ResponseEntity<?> placeOrder(@RequestBody @Valid PlaceOrderRequest request) {
		Long customerId = securityService.getCustomerId();
		DetailCustomerResponse customer = customerService.getCustomerById(customerId);

		List<Long> ids = request.items().stream().map(OrderItemRequest::productId).toList();
		List<ShowProductsResponse> products = productService.getProductsById(ids);

		Set<DetailProductFileResponse> filesReferences = products.stream().map(ShowProductsResponse::getFirstImage).collect(Collectors.toSet());
		filesReferences.forEach(f -> {
			String[] split = f.getFileName().split("/");
			String fileName = split[split.length - 1];

			fileService.copy("products/", f.getFileName(),
								OrderService.awsS3Directory, fileName);
		});

		DetailOrderResponse order = service.placeOrder(customer, request.purchasedViaCart(), request.items(), products);

		DetailCardResponse card = null;
		if (request.payment().cardId() != null) {
			card = cardService.getCardByIdAndCustomerId(request.payment().cardId(), customerId);
		}
		ShowPaymentResponse payment = paymentService.executePayment(request.payment(), order, card);

		ShowDeliveryResponse delivery = null;
		if (payment.paymentStatus() == PaymentStatus.APPROVED_NOT_INTEGRATED) {
			service.paymentApproved(order.getId());

			DetailAddressResponse address = null;
			if (request.delivery().addressId() != null) {
				address = addressService.getAddressByIdAndCustomerId(request.delivery().addressId(), customerId);
			}
			delivery = deliveryService.createDelivery(request.delivery(), address);
			deliveryService.executeDelivery(delivery.id());
		}

		order.addPaymentAndDelivery(payment, delivery);
		return ResponseEntity.ok(Response.success("Order placed successfully!", order));
	}
}
