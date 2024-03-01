package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;

public record CriarPedidoDTO(
			CriarPagamentoDTO pagamento,
			CriarEntregaDTO entrega,
			AdicionarItemPedidoDTO item
		) {

}
