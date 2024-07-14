package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.customer.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProcessadorEntrega {
	
	public abstract boolean executa(Customer customer);

}
