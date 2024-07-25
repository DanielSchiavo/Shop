package br.com.danielschiavo.delivery.model.entity;

import br.com.danielschiavo.delivery.model.valueobject.DeliveryAddress;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "orders_deliveries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "order")
@Builder
@Entity
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_type")
	private DeliveryType deliveryType;

	@Embedded
	private DeliveryAddress deliveryAddress;

	@OneToOne(mappedBy = "delivery")
	private Order order;

}
