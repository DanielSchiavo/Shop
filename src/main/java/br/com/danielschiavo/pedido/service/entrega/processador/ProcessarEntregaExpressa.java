package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;

public class ProcessarEntregaExpressa extends ProcessadorEntrega {

	@Override
	public boolean executa(Cliente cliente) {
		System.out.println("Processando entrega expressa para o cliente " + cliente.getNome());
		return true;
	}

}
