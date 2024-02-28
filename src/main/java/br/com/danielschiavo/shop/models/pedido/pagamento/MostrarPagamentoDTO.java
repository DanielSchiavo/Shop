package br.com.danielschiavo.shop.models.pedido.pagamento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record MostrarPagamentoDTO(
		MetodoPagamento metodoPagamento,
		StatusPagamento statusPagamento,
		MostrarCartaoPedidoDTO cartaoPedido
		) {

}
