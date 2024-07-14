package br.com.danielschiavo.pedido.service.pagamento.processador;


import br.com.danielschiavo.customer.model.entity.Customer;

import java.math.BigDecimal;

public class ProcessarPagamentoCartaoDebito extends ProcessadorPagamento {

	public boolean executa(Customer customer, BigDecimal valorTotal) {
		System.out.println("Processando pagamento no cartão de débito para o customer " + customer.getName() + " ele comprou R$" + valorTotal + " em produtos");
		return true;
	}


}
