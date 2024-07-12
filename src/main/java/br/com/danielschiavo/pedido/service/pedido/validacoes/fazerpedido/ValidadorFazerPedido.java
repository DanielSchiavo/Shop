package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.model.entity.Pedido;

public interface ValidadorFazerPedido {
	
	void validar(Pedido pedido, Cliente cliente);
	
}
