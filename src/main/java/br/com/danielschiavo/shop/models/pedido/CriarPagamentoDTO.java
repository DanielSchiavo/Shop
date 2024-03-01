package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;

public record CriarPagamentoDTO(
				MetodoPagamento metodoPagamento,
				Long idCartao,
				String numeroParcelas
		) {

}
