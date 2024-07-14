package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.pedido.model.entity.Pedido;

public interface ValidadorFazerPedido {
	
	void validar(Pedido pedido, Customer customer);
	
}
