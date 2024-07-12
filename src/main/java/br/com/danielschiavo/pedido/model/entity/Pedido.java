package br.com.danielschiavo.pedido.model.entity;

import br.com.danielschiavo.pedido.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Table(name = "pedidos")
@Entity(name = "Pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Pedido {

	@Id
	@GeneratedValue(generator = "UUID")
	public UUID id;
	
	private BigDecimal valorTotal;

	private String nomeCliente;

	private String cpf;

	private Long clienteId;

	private LocalDateTime dataPedido;

	private Boolean comprouPeloCarrinho;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private StatusPedido statusPedido;

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itemsPedido = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Pagamento pagamento;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Entrega entrega;

	
	
	public List<ItemPedido> getItemsPedido() {
		return Collections.unmodifiableList(this.itemsPedido);
	}

	public void adicionarItemPedido(ItemPedido itemPedido) {
		this.itemsPedido.add(itemPedido);
	}

	public void adicionarItemPedido(List<ItemPedido> itemsPedido) {
		this.itemsPedido.addAll(itemsPedido);
	}
	
}
