package br.com.danielschiavo.shop.models.carrinho.itemcarrinho;

import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="carrinhos_itens")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrinho {

	private Long id;
	
    private Long produtoId;
    
    private Integer quantidade;
    
    @ManyToOne
    private Carrinho carrinho;
	
	
}
