package br.com.danielschiavo.pedido.model.entrega.implementacao;

import br.com.danielschiavo.pedido.model.entrega.ProcessadorEntrega;

public class ProcessarEntregaDigital extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega digital para o cliente " + super.getCliente().getNome());
		return true;
	}

}
