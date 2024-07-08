package br.com.danielschiavo.vendas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

@Table(name = "clientes_carrinhos")
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
	private Long clienteId;
	
	private BigDecimal valorTotal;
	
	@Column(name = "data_e_hora_atualizacao")
	private LocalDateTime dataEHoraAtualizacao;
	
	@OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<ItemCarrinho> itemsCarrinho = new ArrayList<>();
	
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "clienteId")
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
