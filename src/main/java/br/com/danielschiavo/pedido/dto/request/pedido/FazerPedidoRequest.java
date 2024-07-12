package br.com.danielschiavo.pedido.dto.request.pedido;

import java.util.List;

import br.com.danielschiavo.pedido.dto.request.entrega.FormaEntregaRequest;
import br.com.danielschiavo.pedido.dto.request.itempedido.AdicionarItemPedidoRequest;
import br.com.danielschiavo.pedido.dto.request.pagamento.FormaPagamentoRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FazerPedidoRequest(
			@NotNull
			FormaPagamentoRequest pagamento,
			@NotNull
			FormaEntregaRequest entrega,
			@NotNull
			Boolean comprouPeloCarrinho,
			@NotNull
			List<AdicionarItemPedidoRequest> items
		) {

}
