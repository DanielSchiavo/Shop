package br.com.danielschiavo.pedido.dto.request.pagamento;

import br.com.danielschiavo.cliente.model.enums.TipoCartao;

public record CriarCartaoPedidoRequest(
			TipoCartao tipoCartao,
			String numeroDeParcelas,
			String numeroCartao
		) {

}
