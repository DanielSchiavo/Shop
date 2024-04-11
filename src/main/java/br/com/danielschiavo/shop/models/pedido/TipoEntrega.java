package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.pedido.dto.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.ProcessadorEntrega;
import br.com.danielschiavo.shop.models.pedido.entrega.implementacao.ProcessarEntregaCorreios;
import br.com.danielschiavo.shop.models.pedido.entrega.implementacao.ProcessarEntregaDigital;
import br.com.danielschiavo.shop.models.pedido.entrega.implementacao.ProcessarEntregaExpressa;
import br.com.danielschiavo.shop.models.pedido.entrega.implementacao.ProcessarEntregaRetiradaNaLoja;

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
	
	public ProcessadorEntrega getProcessador(CriarPedidoDTO pedidoDTO, Cliente cliente) {
		this.processadorEntrega.setCriarPedidoDTO(pedidoDTO);
		this.processadorEntrega.setCliente(cliente);
		return this.processadorEntrega;
	}
	
	public abstract boolean precisaDeEndereco();
}
