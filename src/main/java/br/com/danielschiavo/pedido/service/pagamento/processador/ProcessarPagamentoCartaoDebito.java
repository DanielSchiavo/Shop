package br.com.danielschiavo.pedido.service.pagamento.processador;


import br.com.danielschiavo.cliente.model.entity.Cliente;

import java.math.BigDecimal;

public class ProcessarPagamentoCartaoDebito extends ProcessadorPagamento {

	public boolean executa(Cliente cliente, BigDecimal valorTotal) {
		System.out.println("Processando pagamento no cartão de débito para o cliente " + cliente.getNome() + " ele comprou R$" + valorTotal + " em produtos");
		return true;
	}


}
