package br.com.danielschiavo.pedido.model.pagamento.implementacao;


import br.com.danielschiavo.pedido.model.pagamento.ProcessadorPagamento;

public class ProcessarPagamentoCartaoDebito extends ProcessadorPagamento {

	public boolean executa() {
		System.out.println("Processando pagamento no cartão de débito para o cliente " + super.getCliente().getNome() + " ele comprou R$" + super.getValorTotal() + " em produtos");
		return true;
	}


}
