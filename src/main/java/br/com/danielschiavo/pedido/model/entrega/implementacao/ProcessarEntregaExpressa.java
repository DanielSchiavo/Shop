package br.com.danielschiavo.pedido.model.entrega.implementacao;

import br.com.danielschiavo.pedido.model.entrega.ProcessadorEntrega;

public class ProcessarEntregaExpressa extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega expressa para o cliente " + super.getCliente().getNome());
		return true;
	}

}
