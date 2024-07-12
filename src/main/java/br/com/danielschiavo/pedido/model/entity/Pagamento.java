package br.com.danielschiavo.pedido.model.entity;

import java.time.LocalDateTime;

import br.com.danielschiavo.pedido.model.valueobject.CartaoPedido;
import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;
import br.com.danielschiavo.pedido.model.enums.StatusPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "pedidos_pagamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "pedido")
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "metodo_pagamento")
	private MetodoPagamento metodoPagamento;

	private Byte numeroParcelas;

	@Embedded
	private CartaoPedido cartaoPedido;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_pagamento")
	private StatusPagamento statusPagamento;

	private LocalDateTime dataPagamento;

	@OneToOne(mappedBy = "pagamento")
	private Pedido pedido;
	
}
