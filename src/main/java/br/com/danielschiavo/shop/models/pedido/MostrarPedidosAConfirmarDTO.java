package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record MostrarPedidosAConfirmarDTO(
        BigDecimal valor_total,
        LocalDateTime data_pedido,
        Long cliente_id,
        String nome_cliente,
        MetodoPagamento metodo_pagamento,
        TipoEntrega tipo_entrega,
        MostrarEnderecoPedidosAConfirmarDTO endereco,
        List<MostrarProdutoPedidosAConfirmarDTO> produtos
) {

    public MostrarPedidosAConfirmarDTO(Pedido pedido) {
        this(
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getClienteId(),
                pedido.getNomeCliente(),
                pedido.getMetodo_pagamento(),
                pedido.getTipo_entrega(),
                new MostrarEnderecoPedidosAConfirmarDTO(pedido.getEnderecoPedido()),
                criarListaDeMostrarProdutosPedidosAConfirmarDTO(pedido)
        );
        
    }

	private static List<MostrarProdutoPedidosAConfirmarDTO> criarListaDeMostrarProdutosPedidosAConfirmarDTO(Pedido pedido) {
		List<MostrarProdutoPedidosAConfirmarDTO> list = new ArrayList<MostrarProdutoPedidosAConfirmarDTO>();
    	pedido.getItemsPedido().forEach(itemPedido -> {
    		list.add(new MostrarProdutoPedidosAConfirmarDTO(itemPedido));
    	});
    	return list;
	}
}