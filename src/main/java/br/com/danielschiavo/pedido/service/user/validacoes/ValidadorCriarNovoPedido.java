package br.com.danielschiavo.pedido.service.user.validacoes;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.model.FazerPedidoRequest;

public interface ValidadorCriarNovoPedido {
	
	void validar(FazerPedidoRequest pedidoDTO, Cliente cliente);
	
}
