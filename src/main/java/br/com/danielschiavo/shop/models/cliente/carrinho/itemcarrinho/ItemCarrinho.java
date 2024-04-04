package br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho;

import br.com.danielschiavo.shop.models.cliente.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.produto.Produto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "carrinhos_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ItemCarrinho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private Integer quantidade;
    
    @OneToOne
    private Produto produto;
	
	@ManyToOne
	private Carrinho carrinho;
}
