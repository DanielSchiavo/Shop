package br.com.danielschiavo.shop.models.pedido.validacoes;

import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;

public interface ValidadorCriarNovoPedido {
	
	void validar(CriarPedidoDTO pedidoDTO);
	
}
