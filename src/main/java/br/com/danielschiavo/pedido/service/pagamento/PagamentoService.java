package br.com.danielschiavo.pedido.service.pagamento;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.pedido.model.valueobject.CartaoPedido;
import br.com.danielschiavo.pedido.model.entity.Pagamento;
import br.com.danielschiavo.pedido.model.enums.StatusPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoService {

    @Autowired
    private CardService cartaoService;

    public Pagamento executarPagamento(Pagamento pagamento, BigDecimal valorTotal, Customer customer) {
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        Long cartaoId = pagamento.getCartaoPedido().getCartaoId();
        if (cartaoId != null) {
            var cartao = cartaoService.getCardByIdAndCustomerId(cartaoId, customer.getId());

            CartaoPedido cartaoPedido = CartaoPedido.builder()
                    .nomeBanco(cartao.getBankName())
                    .numeroCartao(cartao.getCardNumber())
                    .nomeNoCartao(cartao.getNameOnCard())
                    .validadeCartao(cartao.getExpirationDate())
                    .numeroDeParcelas(pagamento.getCartaoPedido().getNumeroDeParcelas())
                    .cardType(cartao.getCardType()).build();

            pagamento.setCartaoPedido(cartaoPedido);
        }

        pagamento.getMetodoPagamento().getProcessador().executa(customer, valorTotal);

        return pagamento;
    }
}
