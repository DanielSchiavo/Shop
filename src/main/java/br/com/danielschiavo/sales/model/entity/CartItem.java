package br.com.danielschiavo.sales.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "customers_carts_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private Integer quantity;
    
    private Long productId;
    
    private BigDecimal subTotal;
    
    @Column(name = "insertion_date_time")
    private LocalDateTime insertionDateTime;
    
    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;
	
	@ManyToOne
	private Cart cart;
}
