package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.endereco.Endereco;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "pedidos_endereco")
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class EnderecoPedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cep;
	private String rua;
	private String numero;
	private String complemento;
	private String bairro;
	private String estado;
	
	@OneToOne
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;

	public EnderecoPedido(Endereco endereco, Pedido pedido) {
		this.cep = endereco.getCep();
		this.rua = endereco.getRua();
		this.numero = endereco.getNumero();
		this.complemento = endereco.getComplemento();
		this.bairro = endereco.getBairro();
		this.estado = endereco.getEstado();
		this.pedido = pedido;
	}
}