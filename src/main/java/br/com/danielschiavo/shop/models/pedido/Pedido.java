package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;
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
	
	@Column(name = "valor_total")
	private BigDecimal valorTotal;
	
	@Column(name = "data_pedido")
	private LocalDateTime dataPedido;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@Column(name = "nome_cliente")
	private String nomeCliente;
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private StatusPedido statusPedido;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_entrega")
	private TipoEntrega tipoEntrega;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "metodo_pagamento")
	private MetodoPagamento metodoPagamento;
	
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private DadosCartao dadosCartao;
	
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private EnderecoPedido enderecoPedido;
	
	@ElementCollection
	@CollectionTable(
			name = "pedidos_items",
			joinColumns = @JoinColumn(name = "carrinho_id")
			)
    private List<ItemPedido> itemsPedido;
	
	public Pedido(Cliente cliente, Endereco endereco) {
		this.dataPedido = LocalDateTime.now();
		
		this.cliente = cliente;
		this.nomeCliente = cliente.getNome();
		this.cpf = cliente.getCpf();
		
		this.enderecoPedido = new EnderecoPedido(endereco, this);
	}

}
