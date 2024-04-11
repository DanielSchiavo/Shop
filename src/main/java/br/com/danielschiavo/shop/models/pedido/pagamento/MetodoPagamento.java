package br.com.danielschiavo.shop.models.pedido.pagamento;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.pedido.dto.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.implementacao.ProcessarPagamentoBoleto;
import br.com.danielschiavo.shop.models.pedido.pagamento.implementacao.ProcessarPagamentoCartaoCredito;
import br.com.danielschiavo.shop.models.pedido.pagamento.implementacao.ProcessarPagamentoCartaoDebito;
import br.com.danielschiavo.shop.models.pedido.pagamento.implementacao.ProcessarPagamentoPix;

public enum MetodoPagamento {
	CARTAO_CREDITO(new ProcessarPagamentoCartaoCredito())
	{
		@Override
		public boolean precisaDeCartao() {
			return true;
		}

		@Override
		public boolean podeParcelar() {
			return true;
		}

		@Override
		public StatusPagamento statusPagamentoDeveSer() {
			return StatusPagamento.EM_PROCESSAMENTO;
		}
	},
 	CARTAO_DEBITO(new ProcessarPagamentoCartaoDebito())
 	{
		@Override
		public boolean precisaDeCartao() {
			return true;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}

		@Override
		public StatusPagamento statusPagamentoDeveSer() {
			return StatusPagamento.EM_PROCESSAMENTO;
		}
	},
 	PIX(new ProcessarPagamentoPix())
 	{
		@Override
		public boolean precisaDeCartao() {
			return false;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}

		@Override
		public StatusPagamento statusPagamentoDeveSer() {
			return StatusPagamento.PENDENTE;
		}
	},
 	BOLETO(new ProcessarPagamentoBoleto()) {
		@Override
		public boolean precisaDeCartao() {
			return false;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}

		@Override
		public StatusPagamento statusPagamentoDeveSer() {
			return StatusPagamento.PENDENTE;
		}
	};
	
	private ProcessadorPagamento processadorPagamento;
	
	MetodoPagamento(ProcessadorPagamento processadorPagamento){
		this.processadorPagamento = processadorPagamento;
	}
	
	public ProcessadorPagamento getProcessador(CriarPedidoDTO pedidoDTO, Cliente cliente) {
		this.processadorPagamento.setCriarPedidoDTO(pedidoDTO);
		this.processadorPagamento.setCliente(cliente);
		return this.processadorPagamento;
	}
	
	public abstract boolean precisaDeCartao();
	
	public abstract boolean podeParcelar();
	
	public abstract StatusPagamento statusPagamentoDeveSer();
}
