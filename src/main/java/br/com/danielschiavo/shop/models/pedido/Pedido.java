package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "pedidos")
@Entity(name = "Pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	private BigDecimal valorTotal;
	
	private LocalDateTime dataPedido;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	private String nomeCliente;
	
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private StatusPedido statusPedido;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Pagamento pagamento;
    
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Entrega entrega;
	
	@ElementCollection
	@CollectionTable(
			name = "pedidos_items",
			joinColumns = @JoinColumn(name = "pedido_id")
			)
    private List<ItemPedido> itemsPedido;
	
	public Pedido(Cliente cliente, String nome, String cpf, StatusPedido statusPedido) {
		this.dataPedido = LocalDateTime.now();
		this.cliente = cliente;
		this.nomeCliente = nome;
		this.cpf = cpf;
		this.statusPedido = statusPedido;
		this.itemsPedido = new ArrayList<>();
	}

}
