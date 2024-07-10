package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.dto.request.pedido.FazerPedidoRequest;

public interface ValidadorFazerPedido {
	
	void validar(FazerPedidoRequest pedidoDTO, Cliente cliente);
	
}
