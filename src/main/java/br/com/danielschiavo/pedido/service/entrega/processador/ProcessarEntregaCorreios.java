package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.pedido.service.entrega.processador.enums.ServicoCorreio;

public class ProcessarEntregaCorreios extends ProcessadorEntrega {

	private ServicoCorreio servico;

	public ProcessarEntregaCorreios(ServicoCorreio servico) {
		this.servico = servico;
	}

	@Override
	public boolean executa(Customer customer) {
		System.out.println("Processando entrega via correios para o customer " + customer.getName());
		return false;
	}

}
