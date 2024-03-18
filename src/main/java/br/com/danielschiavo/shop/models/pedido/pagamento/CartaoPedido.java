package br.com.danielschiavo.shop.models.pedido.pagamento;

import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
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
public class CartaoPedido {
	
	private String nomeBanco;
	private String numeroCartao;
	private String nomeNoCartao;
	private String validadeCartao;
	private String numeroDeParcelas;
	
	@Enumerated(EnumType.STRING)
	private TipoCartao tipoCartao;
	
	public CartaoPedido(Cartao cartao, String numeroDeParcelas) {
		this.nomeBanco = cartao.getNomeBanco();
		this.numeroCartao = cartao.getNumeroCartao();
		this.nomeNoCartao = cartao.getNomeNoCartao();
		this.validadeCartao = cartao.getValidadeCartao();
		this.numeroDeParcelas = numeroDeParcelas;
		this.tipoCartao = cartao.getTipoCartao();
	}
}
