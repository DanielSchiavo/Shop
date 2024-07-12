package br.com.danielschiavo.pedido.service.pagamento.processador;


import br.com.danielschiavo.cliente.model.entity.Cliente;

import java.math.BigDecimal;

public class ProcessarPagamentoPix extends ProcessadorPagamento {
	
	@Override
	public boolean executa(Cliente cliente, BigDecimal valorTotal) {
		System.out.println("Gerando QR Code e chave Copia e Cola do Pix para o cliente " + cliente.getNome() + " ele comprou R$" + valorTotal + " em produtos");
		return true;
	}

}
