package br.com.danielschiavo.delivery.model.valueobject;

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
public class DeliveryAddress {

	private Long addressId;

	private String postalCode;
	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String city;
	private String state;

}