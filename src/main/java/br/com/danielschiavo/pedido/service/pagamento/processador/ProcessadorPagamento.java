package br.com.danielschiavo.pedido.service.pagamento.processador;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class ProcessadorPagamento {

	public abstract boolean executa(Customer customer, BigDecimal valorTotal);
	
}
