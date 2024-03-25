package br.com.danielschiavo.shop.models.pedido.validacoes;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;

public interface ValidadorCriarNovoPedido {
	
	void validar(CriarPedidoDTO pedidoDTO, Cliente cliente);
	
}
