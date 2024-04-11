package br.com.danielschiavo.shop.models.pedido.pagamento.implementacao;

import br.com.danielschiavo.shop.models.pedido.pagamento.ProcessadorPagamento;

public class ProcessarPagamentoCartaoDebito extends ProcessadorPagamento {

	public boolean executa() {
		System.out.println("Processando pagamento no cartão de débito para o cliente " + super.getCliente().getNome() + " ele comprou " + super.getCriarPedidoDTO().items().size() + " items");
		return true;
	}


}
