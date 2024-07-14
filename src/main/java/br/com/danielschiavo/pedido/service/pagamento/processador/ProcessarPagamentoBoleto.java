package br.com.danielschiavo.pedido.service.pagamento.processador;


import br.com.danielschiavo.customer.model.entity.Customer;

import java.math.BigDecimal;

public class ProcessarPagamentoBoleto extends ProcessadorPagamento {

	@Override
	public boolean executa(Customer customer, BigDecimal valorTotal) {
		System.out.println("Gerando boleto de pagamento para o customer " + customer.getName() + " ele comprou R$" + valorTotal + " em produtos" );
		return true;
	}

}
