package br.com.danielschiavo.shop.models.orderitem;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.client.Client;
import br.com.danielschiavo.shop.models.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "order_item")
@Entity(name = "OrderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderItem {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal unitaryValue;
	private Integer quantity;

	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Client client;
}
