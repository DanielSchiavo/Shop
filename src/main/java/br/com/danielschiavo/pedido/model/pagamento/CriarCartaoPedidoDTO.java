package br.com.danielschiavo.pedido.model.pagamento;

import br.com.danielschiavo.cliente.model.enums.TipoCartao;

public record CriarCartaoPedidoDTO(
			TipoCartao tipoCartao,
			String numeroDeParcelas,
			String numeroCartao
		) {

}
