package br.com.danielschiavo.pedido.dto.response.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.danielschiavo.pedido.dto.response.itempedido.MostrarItemPedidoResponse;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.enums.StatusPedido;
import br.com.danielschiavo.pedido.dto.response.pagamento.MostrarCartaoPedidoResponse;
import br.com.danielschiavo.pedido.dto.response.pagamento.MostrarPagamentoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.pedido.dto.response.entrega.MostrarEnderecoPedidoResponse;
import br.com.danielschiavo.pedido.dto.response.entrega.MostrarEntregaResponse;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarPedidoResponse(
		UUID idPedido,
		Long idCliente,
        BigDecimal valorTotal,
        LocalDateTime dataPedido,
        StatusPedido statusPedido,
        MostrarEntregaResponse entrega,
        MostrarPagamentoResponse pagamento,
        List<MostrarItemPedidoResponse> produtos
) {

    public MostrarPedidoResponse(Pedido pedido, List<MostrarItemPedidoResponse> listaMostrarProdutoDoPedidoDTO) {
        this(pedido.getId(),
        	 pedido.getClienteId(),
             pedido.getValorTotal(),
             pedido.getDataPedido(),
             pedido.getStatusPedido(),
             new MostrarEntregaResponse(pedido.getEntrega().getTipoEntrega(),
            		 Optional.ofNullable(pedido.getEntrega().getEnderecoPedido())
            		 .map(MostrarEnderecoPedidoResponse::new)
            		 .orElse(null)),
             new MostrarPagamentoResponse(pedido.getPagamento().getMetodoPagamento(),
				                	 pedido.getPagamento().getStatusPagamento(),
				                	 Optional.ofNullable(pedido.getPagamento().getCartaoPedido())
				                	 .map(cartaoPedido -> new MostrarCartaoPedidoResponse(pedido.getPagamento().getCartaoPedido()))
				                	 .orElse(null)),
             listaMostrarProdutoDoPedidoDTO);
    }

}