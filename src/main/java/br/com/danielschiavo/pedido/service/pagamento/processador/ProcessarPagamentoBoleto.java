package br.com.danielschiavo.pedido.service.pagamento.processador;


import br.com.danielschiavo.cliente.model.entity.Cliente;

import java.math.BigDecimal;

public class ProcessarPagamentoBoleto extends ProcessadorPagamento {

	@Override
	public boolean executa(Cliente cliente, BigDecimal valorTotal) {
		System.out.println("Gerando boleto de pagamento para o cliente " + cliente.getNome() + " ele comprou R$" + valorTotal + " em produtos" );
		return true;
	}

}
