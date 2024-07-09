package br.com.danielschiavo.pedido.dto.response.pagamento;

import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;
import br.com.danielschiavo.pedido.model.enums.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarPagamentoResponse(
		MetodoPagamento metodoPagamento,
		StatusPagamento statusPagamento,
		MostrarCartaoPedidoResponse cartaoPedido
		) {

}
