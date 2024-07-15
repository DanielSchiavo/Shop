package br.com.danielschiavo.pedido.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.customer.CustomerService;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.pedido.model.entity.Delivery;
import br.com.danielschiavo.pedido.model.entity.Order;
import br.com.danielschiavo.pedido.model.entity.Payment;
import br.com.danielschiavo.pedido.model.enums.OrderStatus;
import br.com.danielschiavo.pedido.repository.OrderRepository;
import br.com.danielschiavo.pedido.service.delivery.DeliveryService;
import br.com.danielschiavo.pedido.service.payment.PaymentService;
import br.com.danielschiavo.produto.service.product.ProductService;
import br.com.danielschiavo.shared.exception.ValidationException;
import br.com.danielschiavo.vendas.service.CartService;
import br.com.danielschiavo.filestorage.service.FileStoragePedidoService;
import br.com.danielschiavo.pedido.model.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.service.order.validators.placeorder.ValidatorPlaceOrder;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class OrderService {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartService cartService;

    @Autowired
    private CustomerService customerService;

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;

	@Autowired
	private List<ValidatorPlaceOrder> validators;

	@Autowired
	private OrderRepository repository;

	public Page<Order> getAllOrdersByCustomerId(Pageable pageable, Long customerId) {
		return repository.findAllByCustomerId(pageable, customerId);
	}
	
	@Transactional
	public Order placeOrder(Long customerId, Order order) {
		Customer customer = customerService.getCustomerById(customerId);
		validators.forEach(v -> v.validate(order, customer));

		List<OrderItem> orderItems = order.getOrderItems();
		populateOrderItems(orderItems);

		BigDecimal totalValue = orderItems.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		Payment payment = paymentService.execute(order.getPayment(), totalValue, customer);
		Delivery delivery = deliveryService.execute(order.getDelivery(), customer);

		order.setCustomerName(customer.getName() + " " + customer.getSurname());
		order.setCpf(customer.getCpf());
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.TO_PAY);
		order.addOrderItem(orderItems);
		order.setPayment(payment);
		order.setDelivery(delivery);

		if (order.getPurchasedViaCart()) {
			Long[] ids = order.getOrderItems().stream().map(OrderItem::getProductId).toArray(Long[]::new);
			cartService.removerProdutoDoCarrinho(customerId, ids);
		}

		return repository.save(order);
	}

	public Order getOrderById(UUID orderId) {
		return repository.findById(orderId).orElseThrow(() -> new ValidationException("There's no order with provided id"));
	}

	public Order getOrderByIdAndCustomerId(UUID orderId, Long customerId) {
		return repository.findByIdAndCustomerId(orderId, customerId).orElseThrow(() -> new ValidationException("Customer doens't have a order with provided id"));
	}

	public Page<Order> getAllOrders(Pageable pageable) {
		return repository.findAll(pageable);
	}

//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------

//	------------------------------

	private void populateOrderItems(List<OrderItem> items) {
		items.forEach(item -> {
			var produto = productService.getProductById(item.getProductId());
			BigDecimal subTotal = produto.getPrice().multiply(new BigDecimal(item.getQuantity()));

			File file = fileStoragePedidoService.handleImagemPedido(produto.getNameFirstImage(), produto.getId());

			item.setPrice(produto.getPrice());
			item.setProductName(produto.getName());
			item.setFirstImage(file.getFileName());
			item.setSubTotal(subTotal);
		});
	}
}
