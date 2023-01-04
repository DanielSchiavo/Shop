package br.com.danielschiavo.shop.models.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.danielschiavo.shop.models.client.Client;
import br.com.danielschiavo.shop.models.orderitem.OrderItem;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "orders")
@Entity(name = "Order")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal totalvalue;
	
	private LocalDate date = LocalDate.now();
	
	@ManyToOne
	private Client client;
	
	@OneToMany
	private List<OrderItem> products;

}
