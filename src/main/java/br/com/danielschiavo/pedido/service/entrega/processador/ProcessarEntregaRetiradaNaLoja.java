package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;

public class ProcessarEntregaRetiradaNaLoja extends ProcessadorEntrega {

	@Override
	public boolean executa(Cliente cliente) {
		System.out.println("Processando entrega de retirada na loja para o cliente " + cliente.getNome());
		return true;
	}

}
