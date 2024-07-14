package br.com.danielschiavo.pedido.model.valueobject;

import br.com.danielschiavo.customer.model.entity.Address;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class EnderecoPedido {

	private Long enderecoId;

	private String cep;
	private String rua;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;

	public EnderecoPedido(Address address) {
		this.cep = address.getPostalCode();
		this.rua = address.getStreet();
		this.numero = address.getNumber();
		this.complemento = address.getComplement();
		this.bairro = address.getNeighborhood();
		this.cidade = address.getCity();
		this.estado = address.getState();
	}
}