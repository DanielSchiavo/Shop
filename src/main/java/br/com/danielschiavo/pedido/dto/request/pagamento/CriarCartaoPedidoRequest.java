package br.com.danielschiavo.pedido.dto.request.pagamento;

import br.com.danielschiavo.customer.model.enums.CardType;

public record CriarCartaoPedidoRequest(
			CardType cardType,
			String numeroDeParcelas,
			String numeroCartao
		) {

}
