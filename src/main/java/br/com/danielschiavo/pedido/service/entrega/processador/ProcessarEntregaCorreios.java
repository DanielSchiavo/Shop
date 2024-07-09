package br.com.danielschiavo.pedido.service.entrega.processador;

public class ProcessarEntregaCorreios extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega via correios para o cliente " + super.getCliente().getNome());
		return false;
	}

}
