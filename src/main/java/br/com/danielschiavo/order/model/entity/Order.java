package br.com.danielschiavo.order.model.entity;

import br.com.danielschiavo.order.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Order {

	@Id
	@GeneratedValue(generator = "UUID")
	public UUID id;
	
	private BigDecimal totalValue;

	private String customerName;

	private String cpf;

	private Long customerId;

	private LocalDateTime orderDate;

	private Boolean purchasedViaCart;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private OrderStatus orderStatus;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Payment payment;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Delivery delivery;

	
	
	public List<OrderItem> getOrderItems() {
		return Collections.unmodifiableList(this.orderItems);
	}

	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}

	public void addOrderItem(List<OrderItem> orderItems) {
		this.orderItems.addAll(orderItems);
	}
	
}
