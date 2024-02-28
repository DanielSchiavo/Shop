package br.com.danielschiavo.shop.models.carrinho.itemcarrinho;

import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private Long produtoId;
    
    private Integer quantidade;
    
    @ManyToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;
	
	
}
