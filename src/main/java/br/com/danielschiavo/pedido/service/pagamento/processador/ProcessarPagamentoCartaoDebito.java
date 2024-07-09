package br.com.danielschiavo.pedido.service.pagamento.processador;


public class ProcessarPagamentoCartaoDebito extends ProcessadorPagamento {

	public boolean executa() {
		System.out.println("Processando pagamento no cartão de débito para o cliente " + super.getCliente().getNome() + " ele comprou R$" + super.getValorTotal() + " em produtos");
		return true;
	}


}
