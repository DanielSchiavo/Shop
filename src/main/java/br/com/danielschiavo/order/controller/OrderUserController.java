package br.com.danielschiavo.order.controller;

import br.com.danielschiavo.catalog.dto.response.product.ShowProductsResponse;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.customer.service.address.AddressService;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.customer.service.customer.CustomerService;
import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.delivery.service.DeliveryService;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.order.dto.request.OrderItemRequest;
import br.com.danielschiavo.order.dto.request.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.dto.response.DetailOrderItemResponse;
import br.com.danielschiavo.order.service.OrderService;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import br.com.danielschiavo.payment.service.PaymentService;
import br.com.danielschiavo.shared.DetailFileResponse;
import br.com.danielschiavo.shared.FileMapper;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private FileMapper fileMapper;

	@Autowired
	private FileReferenceService fileService;

	@GetMapping("/{orderId}")
	@Operation(summary = "Get an order by id to get all the details about it")
	public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
		Long customerId = securityService.getCustomerId();

		DetailOrderResponse order = service.getOrderByIdAndCustomerId(orderId, customerId);

		List<DetailFileResponse> listDetailFile = order.getAllDetailFile();
		Set<String> fileNames = order.getAllFileNames();

		fileMapper.mapFilesToDto(listDetailFile, OrderService.awsS3Directory, fileNames);

		return ResponseEntity.ok(Response.success("Success recovering order", order));
	}
	
	@GetMapping
	@Operation(summary = "Get all user orders")
	public ResponseEntity<?> getAllOrders(Pageable pageable) {
		Long customerId = securityService.getCustomerId();

		List<DetailOrderResponse> orders = service.getAllOrdersByCustomerId(pageable, customerId);

		List<DetailFileResponse> listDetailFile = orders.stream()
				.flatMap(order -> order.getAllDetailFile().stream()) // Converte cada lista de items em um único stream
				.toList();

		Set<String> fileNames = orders.stream()
				.flatMap(order -> order.getAllFileNames().stream()) // Achata todos os nomes de arquivos em um único stream
				.collect(Collectors.toSet());

		fileMapper.mapFilesToDto(listDetailFile, OrderService.awsS3Directory, fileNames);

		return ResponseEntity.ok(Response.success("Success recovering all user orders", new PageImpl<>(orders, pageable, orders.size())));
	}
	
	@PostMapping
	@Operation(summary = "Place an order")
	public ResponseEntity<?> placeOrder(@RequestBody @Valid PlaceOrderRequest request) {
		Long customerId = securityService.getCustomerId();
		DetailCustomerResponse customer = customerService.getCustomerById(customerId);

		List<Long> ids = request.items().stream().map(OrderItemRequest::productId).toList();
		List<ShowProductsResponse> products = productService.getProductsById(ids);

		DetailOrderResponse order = service.placeOrder(customer, request.purchasedViaCart(), request.items(), products);

		Set<String> fileNames = order.getAllFileNames();
		List<DetailFileResponse> productFiles = order.getAllDetailFile();

		saveProductsFirstImage(fileNames);
		fileMapper.mapFilesToDto(productFiles, OrderService.awsS3Directory, fileNames);

		ShowPaymentResponse payment = executePayment(request, customerId, order);
		ShowDeliveryResponse delivery = executeDelivery(request, payment, order, customerId);

		order.addPaymentAndDelivery(payment, delivery);
		return ResponseEntity.ok(Response.success("Order placed successfully!", order));
	}

	private void saveProductsFirstImage(Set<String> fileNames) {
		fileNames.forEach(name -> fileService.copy(ProductService.awsS3Directory, name,
												   OrderService.awsS3Directory, name));
	}

	private ShowPaymentResponse executePayment(PlaceOrderRequest request, Long customerId, DetailOrderResponse order) {
		DetailCardResponse card = null;
		if (request.payment().cardId() != null) {
			card = cardService.getCardByIdAndCustomerId(request.payment().cardId(), customerId);
		}
        return paymentService.executePayment(request.payment(), order, card);
	}

	private ShowDeliveryResponse executeDelivery(PlaceOrderRequest request, ShowPaymentResponse payment, DetailOrderResponse order, Long customerId) {
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
		return delivery;
	}
}
