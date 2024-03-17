package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import jakarta.validation.constraints.NotNull;

public record CriarPedidoDTO(
			@NotNull
			CriarPagamentoDTO pagamento,
			@NotNull
			CriarEntregaDTO entrega,
			AdicionarItemPedidoDTO item
		) {

}
