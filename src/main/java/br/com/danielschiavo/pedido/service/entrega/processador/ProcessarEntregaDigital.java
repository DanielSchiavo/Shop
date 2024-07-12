package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;

public class ProcessarEntregaDigital extends ProcessadorEntrega {

	@Override
	public boolean executa(Cliente cliente) {
		System.out.println("Processando entrega digital para o cliente " + cliente.getNome());
		return true;
	}

}
