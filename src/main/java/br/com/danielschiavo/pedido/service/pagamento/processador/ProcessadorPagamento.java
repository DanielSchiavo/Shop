package br.com.danielschiavo.pedido.service.pagamento.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class ProcessadorPagamento {

	public abstract boolean executa(Cliente cliente, BigDecimal valorTotal);
	
}
