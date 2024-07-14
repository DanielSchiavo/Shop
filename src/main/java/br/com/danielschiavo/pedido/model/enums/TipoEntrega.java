package br.com.danielschiavo.pedido.model.enums;

import br.com.danielschiavo.pedido.service.entrega.processador.ProcessadorEntrega;
import br.com.danielschiavo.pedido.service.entrega.processador.ProcessarEntregaCorreios;
import br.com.danielschiavo.pedido.service.entrega.processador.ProcessarEntregaDigital;
import br.com.danielschiavo.pedido.service.entrega.processador.ProcessarEntregaExpressa;
import br.com.danielschiavo.pedido.service.entrega.processador.ProcessarEntregaRetiradaNaLoja;
import br.com.danielschiavo.pedido.service.entrega.processador.enums.ServicoCorreio;

public enum TipoEntrega {
	CORREIOS_SEDEX(new ProcessarEntregaCorreios(ServicoCorreio.SEDEX))
	{
		@Override
		public boolean precisaDeEndereco() {
			return true;
		}
	},
	CORREIOS_PAC(new ProcessarEntregaCorreios(ServicoCorreio.PAC))
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
	
	public ProcessadorEntrega getProcessador() {
		return this.processadorEntrega;
	}
	
	public abstract boolean precisaDeEndereco();
}
