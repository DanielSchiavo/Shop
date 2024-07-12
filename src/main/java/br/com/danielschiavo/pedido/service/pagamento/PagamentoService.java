package br.com.danielschiavo.pedido.service.pagamento;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.cartao.CartaoService;
import br.com.danielschiavo.pedido.dto.request.pagamento.FormaPagamentoRequest;
import br.com.danielschiavo.pedido.model.valueobject.CartaoPedido;
import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;
import br.com.danielschiavo.pedido.model.entity.Pagamento;
import br.com.danielschiavo.pedido.model.enums.StatusPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoService {

    @Autowired
    private CartaoService cartaoService;

    public Pagamento executarPagamento(Pagamento pagamento, BigDecimal valorTotal, Cliente cliente) {
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        Long cartaoId = pagamento.getCartaoPedido().getCartaoId();
        if (cartaoId != null) {
            var cartao = cartaoService.pegarCartao(cartaoId, cliente.getId());

            CartaoPedido cartaoPedido = CartaoPedido.builder()
                    .nomeBanco(cartao.getNomeBanco())
                    .numeroCartao(cartao.getNumeroCartao())
                    .nomeNoCartao(cartao.getNomeNoCartao())
                    .validadeCartao(cartao.getValidadeCartao())
                    .numeroDeParcelas(pagamento.getCartaoPedido().getNumeroDeParcelas())
                    .tipoCartao(cartao.getTipoCartao()).build();

            pagamento.setCartaoPedido(cartaoPedido);
        }

        pagamento.getMetodoPagamento().getProcessador().executa(cliente, valorTotal);

        return pagamento;
    }
}
