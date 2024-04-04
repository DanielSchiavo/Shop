package br.com.danielschiavo.shop.models.pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.MostrarEnderecoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.MostrarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.MostrarCartaoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.MostrarPagamentoDTO;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarPedidoDTO(
		UUID idPedido,
		Long idCliente,
        BigDecimal valorTotal,
        LocalDateTime dataPedido,
        StatusPedido statusPedido,
        MostrarEntregaDTO entrega,
        MostrarPagamentoDTO pagamento,
        List<MostrarProdutoDoPedidoDTO> produtos
) {

    public MostrarPedidoDTO(Pedido pedido, List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO) {
        this(pedido.getId(),
        	 pedido.getCliente().getId(),
             pedido.getValorTotal(),
             pedido.getDataPedido(),
             pedido.getStatusPedido(),
             new MostrarEntregaDTO(pedido.getEntrega().getTipoEntrega(),
            		 Optional.ofNullable(pedido.getEntrega().getEnderecoPedido())
            		 .map(enderecoPedido -> new MostrarEnderecoPedidoDTO(enderecoPedido))
            		 .orElse(null)),
             new MostrarPagamentoDTO(pedido.getPagamento().getMetodoPagamento(), 
				                	 pedido.getPagamento().getStatusPagamento(),
				                	 Optional.ofNullable(pedido.getPagamento().getCartaoPedido())
				                	 .map(cartaoPedido -> new MostrarCartaoPedidoDTO(pedido.getPagamento().getCartaoPedido()))
				                	 .orElse(null)),
             listaMostrarProdutoDoPedidoDTO);
    }

}