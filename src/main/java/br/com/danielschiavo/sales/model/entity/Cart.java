package br.com.danielschiavo.sales.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "customers_carts")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class Cart {

	@Id 
	private Long customerId;
	
	private BigDecimal totalValue;
	
	@Column(name = "update_date_time")
	private LocalDateTime updateDateTime;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<CartItem> cartItems = new ArrayList<>();
	

	public List<CartItem> getCartItems() {
		return Collections.unmodifiableList(this.cartItems);
	}

	public void addCartItem(CartItem cartItem) {
		totalValue = totalValue.add(cartItem.getSubTotal());
		this.cartItems.add(cartItem);
	}
	
	public void removeCartItem(CartItem cartItem) {
		totalValue = totalValue.subtract(cartItem.getSubTotal());
		this.cartItems.remove(cartItem);
	}
	
}
