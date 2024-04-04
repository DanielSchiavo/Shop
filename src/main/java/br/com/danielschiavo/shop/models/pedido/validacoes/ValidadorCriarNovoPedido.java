package br.com.danielschiavo.shop.models.pedido.validacoes;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.pedido.dto.CriarPedidoDTO;

public interface ValidadorCriarNovoPedido {
	
	void validar(CriarPedidoDTO pedidoDTO, Cliente cliente);
	
}
