package br.com.danielschiavo.pedido.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.danielschiavo.pedido.model.pagamento.MostrarCartaoPedidoDTO;
import br.com.danielschiavo.pedido.model.pagamento.MostrarPagamentoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.pedido.model.entrega.MostrarEnderecoPedidoDTO;
import br.com.danielschiavo.pedido.model.entrega.MostrarEntregaDTO;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarPedidoResponse(
		UUID idPedido,
		Long idCliente,
        BigDecimal valorTotal,
        LocalDateTime dataPedido,
        StatusPedido statusPedido,
        MostrarEntregaDTO entrega,
        MostrarPagamentoDTO pagamento,
        List<MostrarProdutoDoPedidoResponse> produtos
) {

    public MostrarPedidoResponse(Pedido pedido, List<MostrarProdutoDoPedidoResponse> listaMostrarProdutoDoPedidoDTO) {
        this(pedido.getId(),
        	 pedido.getClienteId(),
             pedido.getValorTotal(),
             pedido.getDataPedido(),
             pedido.getStatusPedido(),
             new MostrarEntregaDTO(pedido.getEntrega().getTipoEntrega(),
            		 Optional.ofNullable(pedido.getEntrega().getEnderecoPedido())
            		 .map(MostrarEnderecoPedidoDTO::new)
            		 .orElse(null)),
             new MostrarPagamentoDTO(pedido.getPagamento().getMetodoPagamento(), 
				                	 pedido.getPagamento().getStatusPagamento(),
				                	 Optional.ofNullable(pedido.getPagamento().getCartaoPedido())
				                	 .map(cartaoPedido -> new MostrarCartaoPedidoDTO(pedido.getPagamento().getCartaoPedido()))
				                	 .orElse(null)),
             listaMostrarProdutoDoPedidoDTO);
    }

}