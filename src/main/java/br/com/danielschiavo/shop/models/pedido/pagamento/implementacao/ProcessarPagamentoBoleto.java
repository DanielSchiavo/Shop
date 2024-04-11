package br.com.danielschiavo.shop.models.pedido.pagamento.implementacao;

import br.com.danielschiavo.shop.models.pedido.pagamento.ProcessadorPagamento;

public class ProcessarPagamentoBoleto extends ProcessadorPagamento {

	@Override
	public boolean executa() {
		System.out.println("Gerando boleto de pagamento para o cliente " + super.getCliente().getNome() + " ele comprou " + super.getCriarPedidoDTO().items().size() + " items");
		return true;
	}

}
