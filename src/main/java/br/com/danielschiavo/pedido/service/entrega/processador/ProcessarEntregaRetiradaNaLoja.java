package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.customer.model.entity.Customer;

public class ProcessarEntregaRetiradaNaLoja extends ProcessadorEntrega {

	@Override
	public boolean executa(Customer customer) {
		System.out.println("Processando entrega de retirada na loja para o customer " + customer.getName());
		return true;
	}

}
