package br.com.danielschiavo.pedido.dto.response.pagamento;

import br.com.danielschiavo.customer.model.enums.CardType;
import br.com.danielschiavo.pedido.model.valueobject.CartaoPedido;

public record MostrarCartaoPedidoResponse(
		String nomeBanco,
		String numeroCartao,
		String nomeNoCartao,
		String numeroDeParcelas,
		CardType cardType
		) {
	
    public MostrarCartaoPedidoResponse(CartaoPedido dadosCartao) {
        this(
        		dadosCartao.getNomeBanco(),
        		dadosCartao.getNumeroCartao(),
        		dadosCartao.getNomeNoCartao(),
        		dadosCartao.getNumeroDeParcelas(),
        		dadosCartao.getCardType()
        );
    }
}
