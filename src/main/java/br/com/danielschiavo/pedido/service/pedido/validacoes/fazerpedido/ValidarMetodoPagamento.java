package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.service.cartao.CartaoService;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;

@Service
public class ValidarMetodoPagamento implements ValidadorFazerPedido {
	
	@Autowired
	private CartaoService cartaoService;
	
	@Override
	public void validar(Pedido pedido, Cliente cliente, Long cartaoId, Long enderecoId) {
		MetodoPagamento metodoPagamento = pedido.getPagamento().getMetodoPagamento();
		String numeroParcelas = pedido.getPagamento().getCartaoPedido().getNumeroDeParcelas();

		//Metodo de Pagamento precisa de cartão mas o ID do cartão não foi enviado || Metodo de Pagamento pode par
		if ((metodoPagamento.precisaDeCartao() && idCartao == null) || (metodoPagamento.podeParcelar() && numeroParcelas == null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamento + ", portanto, é necessário enviar o cartaoId do cliente e o numeroParcelas juntamente.");
		}
		if ((!metodoPagamento.precisaDeCartao() && idCartao != null) || (!metodoPagamento.podeParcelar() && numeroParcelas != null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamento + ", portanto, você não deve enviar um cartaoId nem o numeroParcelas junto.");
		}
		
		if (idCartao != null) {
			Cartao cartao = cartaoService.pegarCartao(idCartao, cliente.getId());
			if (!metodoPagamento.toString().endsWith(cartao.getTipoCartao().toString())) {
				throw new ValidacaoException("O cartão cadastrado de id número " + cartao.getId() + ", foi cadastrado como um cartão de " + cartao.getTipoCartao().toString() + ", não condiz com o método de pagamento fornecido, que é: " + metodoPagamento.toString());
			}
		}
	}
}
