package br.com.danielschiavo.pedido.service.entrega.processador;

public class ProcessarEntregaRetiradaNaLoja extends ProcessadorEntrega {

	@Override
	public boolean executa() {
		System.out.println("Processando entrega de retirada na loja para o cliente " + super.getCliente().getNome());
		return true;
	}

}
