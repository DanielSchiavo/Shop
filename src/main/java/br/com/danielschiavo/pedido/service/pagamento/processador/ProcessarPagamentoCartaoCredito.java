package br.com.danielschiavo.pedido.service.pagamento.processador;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProcessarPagamentoCartaoCredito extends ProcessadorPagamento {

	@Override
	public boolean executa(Customer customer, BigDecimal valorTotal) {
		System.out.println("Processando pagamento no cartão de crédito para o customer " + customer.getName() + " ele comprou R$" + valorTotal + " em produtos");
		return true;
	}

}
