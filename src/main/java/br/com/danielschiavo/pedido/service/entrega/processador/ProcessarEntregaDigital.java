package br.com.danielschiavo.pedido.service.entrega.processador;

public class ProcessarEntregaDigital extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega digital para o cliente " + super.getCliente().getNome());
		return true;
	}

}
