package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.service.cartao.CardService;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.valueobject.CartaoPedido;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;

@Service
public class ValidarMetodoPagamento implements ValidadorFazerPedido {
	
	@Autowired
	private CardService cartaoService;
	
	@Override
	public void validar(Pedido pedido, Customer customer) {
		MetodoPagamento metodoPagamento = pedido.getPagamento().getMetodoPagamento();
		CartaoPedido cartaoPedido = pedido.getPagamento().getCartaoPedido();
		String numeroParcelas = cartaoPedido.getNumeroDeParcelas();
		Long cartaoId = cartaoPedido.getCartaoId();

		//Metodo de Pagamento precisa de cartão mas o ID do cartão não foi enviado || Metodo de Pagamento pode par
		if ((metodoPagamento.precisaDeCartao() && cartaoId == null) || (metodoPagamento.podeParcelar() && numeroParcelas == null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamento + ", portanto, é necessário enviar o cartaoId do customer e o numeroParcelas juntamente.");
		}
		if ((!metodoPagamento.precisaDeCartao() && cartaoId != null) || (!metodoPagamento.podeParcelar() && numeroParcelas != null)) {
			throw new ValidacaoException("O método de pagamento escolhido foi " + metodoPagamento + ", portanto, você não deve enviar um cartaoId nem o numeroParcelas junto.");
		}
		
		if (cartaoId != null) {
			Card card = cartaoService.getCardByIdAndCustomerId(cartaoId, customer.getId());
			if (!metodoPagamento.toString().endsWith(card.getCardType().toString())) {
				throw new ValidacaoException("O cartão cadastrado de id número " + card.getId() + ", foi cadastrado como um cartão de " + card.getCardType().toString() + ", não condiz com o método de pagamento fornecido, que é: " + metodoPagamento.toString());
			}
		}
	}
}
