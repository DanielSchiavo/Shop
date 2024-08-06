package br.com.danielschiavo.customer.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "customers_addresses")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Address {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String postalCode;
	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String city;
	
	private String state;
	
	private Boolean isDefault;
	
	private Long customerId;

}