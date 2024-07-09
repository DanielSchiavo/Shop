package br.com.danielschiavo.pedido.service.entrega.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ProcessadorEntrega {
	
	private Cliente cliente;
	
	public abstract boolean executa();

}
