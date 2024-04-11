package br.com.danielschiavo.shop.models.pedido.entrega.implementacao;

import br.com.danielschiavo.shop.models.pedido.entrega.ProcessadorEntrega;

public class ProcessarEntregaCorreios extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega via correios para o cliente " + super.getCliente().getNome());
		return false;
	}

}
