package br.com.danielschiavo.shop.models.pedido.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.services.CartaoService;

@Service
public class ValidadorMetodoPagamento implements ValidadorCriarNovoPedido {
	
	@Autowired
	private CartaoService cartaoService;
	
	@Override
	public void validar(CriarPedidoDTO pedidoDTO) {
		MetodoPagamento metodoPagamentoDTO = pedidoDTO.pagamento().metodoPagamento();
		Long idCartao = pedidoDTO.pagamento().idCartao();
		String numeroParcelas = pedidoDTO.pagamento().numeroParcelas();
		if (metodoPagamentoDTO == MetodoPagamento.CARTAO_CREDITO || metodoPagamentoDTO == MetodoPagamento.CARTAO_DEBITO) {
			if (idCartao == null) {
				throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, é necessário enviar o ID do cartão juntamente.");
			}
			if (numeroParcelas == null) {
				throw new ValidacaoException("É necessário enviar também o número de parcelas, mesmo que seja em 1x");
			}
			cartaoService.verificarSeCartaoExistePorIdCartaoECliente(idCartao);
		}
	}
}
