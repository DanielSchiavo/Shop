package br.com.danielschiavo.shop.models.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	private String cep;
	private String street;
	private String number;
	private String complement;
	private String district;
	private String uf;

}