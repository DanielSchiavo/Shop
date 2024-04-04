package br.com.danielschiavo.shop.models.pedido.dto;

import java.util.List;

import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CriarPedidoDTO(
			@NotNull
			CriarPagamentoDTO pagamento,
			@NotNull
			CriarEntregaDTO entrega,
			@NotNull
			Boolean veioPeloCarrinho,
			@NotNull
			List<AdicionarItemPedidoDTO> items
		) {

}
