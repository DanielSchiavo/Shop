package br.com.danielschiavo.shop.models.pedido.itempedido;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.pedido.Pedido;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "pedidos_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class ItemPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal preco;
	
	private Integer quantidade;
	
	private String nomeProduto;
	
	private String primeiraImagem;
	
	private BigDecimal subTotal;
	
	private Long produtoId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Pedido pedido;

}
