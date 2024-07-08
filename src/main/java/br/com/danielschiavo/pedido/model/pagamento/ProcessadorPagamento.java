package br.com.danielschiavo.pedido.model.pagamento;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class ProcessadorPagamento {

	private BigDecimal valorTotal;
	
	private Cliente cliente;
	
	public abstract boolean executa();
	
}
