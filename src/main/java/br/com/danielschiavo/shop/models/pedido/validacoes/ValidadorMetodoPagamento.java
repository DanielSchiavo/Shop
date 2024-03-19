package br.com.danielschiavo.shop.models.pedido.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cartao.Cartao;
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
			Cartao cartao = cartaoService.verificarSeCartaoExistePorIdCartaoECliente(idCartao);
			if (!metodoPagamentoDTO.toString().endsWith(cartao.getTipoCartao().toString())) {
				throw new ValidacaoException("O cartão cadastrado de id número " + cartao.getId() + ", foi cadastrado como um cartão de " + cartao.getTipoCartao().toString() + ", não condiz com o método de pagamento fornecido, que é: " + metodoPagamentoDTO.toString());
			}
			if (metodoPagamentoDTO == MetodoPagamento.CARTAO_CREDITO && numeroParcelas == null) {
				throw new ValidacaoException("É necessário enviar também o número de parcelas, mesmo que seja em 1x");
			}
			if ((metodoPagamentoDTO == MetodoPagamento.CARTAO_DEBITO && numeroParcelas != null)) {
				throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, você não pode parcelar uma compra no débito, remova o campo numeroParcelas");
			}
		}
		else {
			if (idCartao != null) {
				throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, você não deve enviar um ID de cartão junto.");
			}
			if (numeroParcelas != null) {
				throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, você não deve enviar o número de parcelas junto.");
			}
		}
	}
}
