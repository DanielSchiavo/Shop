package br.com.danielschiavo.pedido.model.enums;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.service.pagamento.processador.ProcessadorPagamento;
import br.com.danielschiavo.pedido.service.pagamento.processador.ProcessarPagamentoBoleto;
import br.com.danielschiavo.pedido.service.pagamento.processador.ProcessarPagamentoCartaoCredito;
import br.com.danielschiavo.pedido.service.pagamento.processador.ProcessarPagamentoCartaoDebito;
import br.com.danielschiavo.pedido.service.pagamento.processador.ProcessarPagamentoPix;

import java.math.BigDecimal;

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
	
	public ProcessadorPagamento getProcessador(BigDecimal valorTotal, Cliente cliente) {
		this.processadorPagamento.setValorTotal(valorTotal);
		this.processadorPagamento.setCliente(cliente);
		return this.processadorPagamento;
	}
	
	public abstract boolean precisaDeCartao();
	
	public abstract boolean podeParcelar();
	
	public abstract StatusPagamento statusPagamentoDeveSer();
}
