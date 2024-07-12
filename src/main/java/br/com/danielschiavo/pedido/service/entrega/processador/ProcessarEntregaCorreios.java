package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.service.entrega.processador.enums.ServicoCorreio;

public class ProcessarEntregaCorreios extends ProcessadorEntrega {

	private ServicoCorreio servico;

	public ProcessarEntregaCorreios(ServicoCorreio servico) {
		this.servico = servico;
	}

	@Override
	public boolean executa(Cliente cliente) {
		System.out.println("Processando entrega via correios para o cliente " + cliente.getNome());
		return false;
	}

}
