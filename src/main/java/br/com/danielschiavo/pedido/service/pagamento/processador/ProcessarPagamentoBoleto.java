package br.com.danielschiavo.pedido.service.pagamento.processador;


public class ProcessarPagamentoBoleto extends ProcessadorPagamento {

	@Override
	public boolean executa() {
		System.out.println("Gerando boleto de pagamento para o cliente " + super.getCliente().getNome() + " ele comprou R$" + super.getValorTotal() + " em produtos" );
		return true;
	}

}
