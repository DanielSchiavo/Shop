package br.com.danielschiavo.shop.models.cliente.carrinho;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho.ItemCarrinho;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "carrinhos")
@Entity(name = "Carrinho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class Carrinho {
	

	@Id 
	private Long id;
	
	private LocalDateTime dataAtualizacao;
	
	@OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<ItemCarrinho> itemsCarrinho = new ArrayList<>();
	
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
	private Cliente cliente;
	
    
	public List<ItemCarrinho> getItemsCarrinho() {
		return Collections.unmodifiableList(this.itemsCarrinho);
	}

	public void adicionarItemCarrinho(ItemCarrinho itemCarrinho) {
		this.itemsCarrinho.add(itemCarrinho);
	}
	
	public void removerItemCarrinho(ItemCarrinho itemCarrinho) {
		this.itemsCarrinho.remove(itemCarrinho);
	}
	
}
