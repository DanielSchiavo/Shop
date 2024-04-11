package br.com.danielschiavo.shop.models.pedido.pagamento.implementacao;

import br.com.danielschiavo.shop.models.pedido.pagamento.ProcessadorPagamento;

public class ProcessarPagamentoPix extends ProcessadorPagamento {
	
	@Override
	public boolean executa() {
		System.out.println("Gerando QR Code e chave Copia e Cola do Pix para o cliente " + super.getCliente().getNome() + " ele comprou " + super.getCriarPedidoDTO().items().size() + " items");
		return true;
	}

}
