package br.com.danielschiavo.pedido.model;

import java.util.List;

import br.com.danielschiavo.pedido.model.entrega.FormaEntregaRequest;
import br.com.danielschiavo.pedido.model.itempedido.AdicionarItemPedidoDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FazerPedidoRequest(
			@NotNull
			FormaPagamentoRequest pagamento,
			@NotNull
			FormaEntregaRequest entrega,
			@NotNull
			Boolean veioPeloCarrinho,
			@NotNull
			List<AdicionarItemPedidoDTO> items
		) {

}
