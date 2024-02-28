package br.com.danielschiavo.shop.models.pedido.pagamento;

import br.com.danielschiavo.shop.models.cartao.TipoCartao;

public record CriarCartaoPedidoDTO(
			TipoCartao tipoCartao,
			String numeroDeParcelas,
			String numeroCartao
		) {

}
