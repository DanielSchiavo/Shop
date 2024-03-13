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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	private String nomeCliente;
	
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private StatusPedido statusPedido;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itemsPedido = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Pagamento pagamento;
    
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Entrega entrega;
	
	@ManyToOne
	private Cliente cliente;
	
	public Pedido(Cliente cliente, String nome, String cpf, StatusPedido statusPedido) {
		this.dataPedido = LocalDateTime.now();
		this.nomeCliente = nome;
		this.cpf = cpf;
		this.statusPedido = statusPedido;
		this.cliente = cliente;
	}

}
