package br.com.danielschiavo.shop.models.pedido.pagamento;

import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import jakarta.persistence.Column;
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
	@Column(name = "tipo_cartao")
	private TipoCartao tipoCartao;
	
}
