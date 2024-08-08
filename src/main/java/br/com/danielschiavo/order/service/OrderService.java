package br.com.danielschiavo.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.dto.response.product.ShowProductsResponse;
import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.order.dto.request.OrderItemRequest;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.mapper.OrderMapper;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.model.enums.OrderStatus;
import br.com.danielschiavo.order.repository.OrderRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import br.com.danielschiavo.order.model.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private OrderMapper mapper;

	public static final String awsS3Directory = "orders";

	public List<DetailOrderResponse> getAllOrdersByCustomerId(Pageable pageable, Long customerId) {
		Page<Order> pageOrder = repository.findAllByCustomerId(pageable, customerId);

		return pageOrder.getContent().stream().map(mapper::toShowOrder).toList();
	}
	
	@Transactional
	public DetailOrderResponse placeOrder(DetailCustomerResponse customer, boolean purchasedViaCart,
										  List<OrderItemRequest> orderItemsRequest, List<ShowProductsResponse> products) {
		List<OrderItem> orderItems = orderItemsRequest.stream()
				.map(request -> {
					ShowProductsResponse product = products.stream()
							.filter(p -> p.getId().equals(request.productId()))
							.findFirst()
							.orElseThrow(() -> new ValidationException("Product not found"));

					if (request.quantity() > product.getQuantity()) {
						throw new ValidationException("Could not proceed with order because the product does not sufficient stock");
					}

					OrderItem orderItem = new OrderItem();
					orderItem.setPrice(product.getPrice());
					orderItem.setQuantity(request.quantity());
					orderItem.setName(product.getName());
					orderItem.setFirstImage(product.getFirstImage().getFileData().getFileName());
					orderItem.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
					orderItem.setProductId(product.getId());

					return orderItem;
				})
				.collect(Collectors.toList());

		BigDecimal totalValue = orderItems.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		Order order = new Order();
		order.setTotalValue(totalValue);
		order.setCustomerName(customer.name() + " " + customer.surname());
		order.setCpf(customer.cpf());
		order.setCustomerId(customer.id());
		order.setOrderTime(LocalDateTime.now());
		order.setExpiryTime(LocalDateTime.now().plusDays(1));
		order.setPurchasedViaCart(purchasedViaCart);
		order.setOrderStatus(OrderStatus.TO_PAY);
		order.addOrderItem(orderItems);

		return mapper.toShowOrder(repository.save(order));
	}

	public DetailOrderResponse getOrderById(UUID orderId) {
		Order order = repository.findById(orderId).orElseThrow(() -> new ValidationException("There's no order with provided id"));
		return mapper.toShowOrder(repository.save(order));
	}

	public DetailOrderResponse getOrderByIdAndCustomerId(UUID orderId, Long customerId) {
		Order order = repository.findByIdAndCustomerId(orderId, customerId)
				.orElseThrow(() -> new ValidationException("Customer doens't have a order with provided id"));

		return mapper.toShowOrder(repository.save(order));
	}

	public List<DetailOrderResponse> getAllOrders(Pageable pageable) {
		Page<Order> all = repository.findAll(pageable);

		return all.getContent().stream().map(mapper::toShowOrder).toList();
	}

	public void paymentApproved(UUID orderId) {
		Order order = repository.findById(orderId).orElseThrow(() -> new ValidationException("There is no order with provided id"));
		order.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);
		repository.save(order);
	}
}
