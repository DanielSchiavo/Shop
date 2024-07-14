package br.com.danielschiavo.pedido.model.valueobject;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.model.enums.CardType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartaoPedido {

	private Long cartaoId;
	
	private String nomeBanco;
	private String numeroCartao;
	private String nomeNoCartao;
	private String validadeCartao;
	private String numeroDeParcelas;
	
	@Enumerated(EnumType.STRING)
	private CardType cardType;
	
	public CartaoPedido(Card card, String numeroDeParcelas) {
		this.nomeBanco = card.getBankName();
		this.numeroCartao = card.getCardNumber();
		this.nomeNoCartao = card.getNameOnCard();
		this.validadeCartao = card.getExpirationDate();
		this.numeroDeParcelas = numeroDeParcelas;
		this.cardType = card.getCardType();
	}
}
