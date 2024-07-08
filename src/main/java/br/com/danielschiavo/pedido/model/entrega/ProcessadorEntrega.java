package br.com.danielschiavo.pedido.model.entrega;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProcessadorEntrega {
	
	private Cliente cliente;
	
	public abstract boolean executa();

}
