package br.com.danielschiavo.pedido.model.pagamento.implementacao;


import br.com.danielschiavo.pedido.model.pagamento.ProcessadorPagamento;

public class ProcessarPagamentoPix extends ProcessadorPagamento {
	
	@Override
	public boolean executa() {
		System.out.println("Gerando QR Code e chave Copia e Cola do Pix para o cliente " + super.getCliente().getNome() + " ele comprou R$" + super.getValorTotal() + " em produtos");
		return true;
	}

}
