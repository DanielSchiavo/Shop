package br.com.danielschiavo.customer.model.entity;

import br.com.danielschiavo.customer.model.enums.CardType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "customers_cards")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Card {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String bankName;
	
	private String cardNumber;
	
	private String nameOnCard;
	
	private String expirationDate;
	
	private Boolean isDefault;
	
	@Enumerated(EnumType.STRING)
	private CardType cardType;
	
	private Long customerId;
	
}