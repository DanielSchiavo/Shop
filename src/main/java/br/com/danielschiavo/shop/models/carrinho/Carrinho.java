package br.com.danielschiavo.shop.models.carrinho;

import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "carrinhos")
@Entity(name = "Carrinho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Carrinho {
	

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Cliente cliente;

	@OneToMany(mappedBy = "carrinho" , cascade = CascadeType.ALL)
    private List<ItemCarrinho> itensCarrinho;
	
	public Carrinho(Cliente cliente, Long produtoId, Integer quantidade) {
		this.cliente = cliente;
		this.itensCarrinho = new ArrayList<>();
		itensCarrinho.add(new ItemCarrinho(null, produtoId, quantidade, this));		
	}
}
