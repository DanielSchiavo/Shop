package br.com.danielschiavo.shop.models.pedido.entrega;

import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "pedidos_entrega")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Entrega {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_entrega")
	private TipoEntrega tipoEntrega;
	
	@Embedded
	private EnderecoPedido enderecoPedido;
	
	@OneToOne(mappedBy = "entrega")
	private Pedido pedido;
	
	public Entrega(Pedido pedido, TipoEntrega tipoEntrega, EnderecoPedido enderecoPedido) {
		this.pedido = pedido;
		this.tipoEntrega = tipoEntrega;
		this.enderecoPedido = enderecoPedido;
	}

}
