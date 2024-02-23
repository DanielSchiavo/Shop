package br.com.danielschiavo.shop.models.carrinho;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
	
	private Long clienteId;

	@ElementCollection
	@CollectionTable(
			name = "carrinhos_itens",
			joinColumns = @JoinColumn(name = "carrinho_id")
			)
    private List<ItemCarrinho> itensCarrinho;
	
	public Carrinho(Long clienteId, Long produtoId, Integer quantidade) {
		this.clienteId = clienteId;
		this.itensCarrinho = new ArrayList<>();
		itensCarrinho.add(new ItemCarrinho(produtoId, quantidade));		
	}
}
