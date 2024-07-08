package br.com.danielschiavo.pedido.model;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.model.entrega.ProcessadorEntrega;
import br.com.danielschiavo.pedido.model.entrega.implementacao.ProcessarEntregaCorreios;
import br.com.danielschiavo.pedido.model.entrega.implementacao.ProcessarEntregaDigital;
import br.com.danielschiavo.pedido.model.entrega.implementacao.ProcessarEntregaExpressa;
import br.com.danielschiavo.pedido.model.entrega.implementacao.ProcessarEntregaRetiradaNaLoja;

public enum TipoEntrega {
	CORREIOS(new ProcessarEntregaCorreios())
	{
		@Override
		public boolean precisaDeEndereco() {
			return true;
		}
	},
 	ENTREGA_EXPRESSA(new ProcessarEntregaExpressa())
 	{
		@Override
		public boolean precisaDeEndereco() {
			return true;
		}
	},
 	RETIRADA_NA_LOJA(new ProcessarEntregaRetiradaNaLoja())
 	{
		@Override
		public boolean precisaDeEndereco() {
			return false;
		}
	},
 	ENTREGA_DIGITAL(new ProcessarEntregaDigital())
 	{
		@Override
		public boolean precisaDeEndereco() {
			return false;
		}
	};
	
	private ProcessadorEntrega processadorEntrega;
	
	TipoEntrega(ProcessadorEntrega processadorEntrega){
		this.processadorEntrega = processadorEntrega;
	}
	
	public ProcessadorEntrega getProcessador(Cliente cliente) {
		this.processadorEntrega.setCliente(cliente);
		return this.processadorEntrega;
	}
	
	public abstract boolean precisaDeEndereco();
}
