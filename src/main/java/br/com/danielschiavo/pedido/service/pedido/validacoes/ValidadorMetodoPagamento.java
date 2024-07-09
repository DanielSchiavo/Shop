package br.com.danielschiavo.pedido.service.pedido.validacoes;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.service.CartaoService;
import br.com.danielschiavo.pedido.dto.request.pedido.FazerPedidoRequest;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;

@Service
public class ValidadorMetodoPagamento implements ValidadorCriarNovoPedido {
	
	@Autowired
	private CartaoService cartaoService;
	
	@Override
	public void validar(FazerPedidoRequest pedidoDTO, Cliente cliente) {
		MetodoPagamento metodoPagamentoDTO = pedidoDTO.pagamento().metodoPagamento();
		Long idCartao = pedidoDTO.pagamento().cartaoId();
		String numeroParcelas = pedidoDTO.pagamento().numeroParcelas();
		
		if ((metodoPagamentoDTO.precisaDeCartao() && idCartao == null) || (metodoPagamentoDTO.podeParcelar() && numeroParcelas == null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, é necessário enviar o cartaoId do cliente e o numeroParcelas juntamente.");
		}
		if ((!metodoPagamentoDTO.precisaDeCartao() && idCartao != null) || (!metodoPagamentoDTO.podeParcelar() && numeroParcelas != null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamentoDTO + ", portanto, você não deve enviar um cartaoId nem o numeroParcelas junto.");
		}
		
		if (idCartao != null) {
			Cartao cartao = cartaoService.pegarCartaoPorIdECliente(idCartao, cliente);
			if (!metodoPagamentoDTO.toString().endsWith(cartao.getTipoCartao().toString())) {
				throw new ValidacaoException("O cartão cadastrado de id número " + cartao.getId() + ", foi cadastrado como um cartão de " + cartao.getTipoCartao().toString() + ", não condiz com o método de pagamento fornecido, que é: " + metodoPagamentoDTO.toString());
			}
		}
	}
}
