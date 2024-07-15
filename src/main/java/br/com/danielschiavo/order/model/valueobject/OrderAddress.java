package br.com.danielschiavo.order.model.valueobject;

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
public class OrderAddress {

	private Long addressId;

	private String postalCode;
	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String city;
	private String state;

	public OrderAddress(Address address) {
		this.postalCode = address.getPostalCode();
		this.street = address.getStreet();
		this.number = address.getNumber();
		this.complement = address.getComplement();
		this.neighborhood = address.getNeighborhood();
		this.city = address.getCity();
		this.state = address.getState();
	}
}