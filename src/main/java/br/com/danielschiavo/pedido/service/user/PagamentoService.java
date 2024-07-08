package br.com.danielschiavo.pedido.service.user;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.CartaoService;
import br.com.danielschiavo.pedido.model.FormaPagamentoRequest;
import br.com.danielschiavo.pedido.model.pagamento.CartaoPedido;
import br.com.danielschiavo.pedido.model.pagamento.MetodoPagamento;
import br.com.danielschiavo.pedido.model.pagamento.Pagamento;
import br.com.danielschiavo.pedido.model.pagamento.StatusPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoService {

    @Autowired
    private CartaoService cartaoService;

    public Pagamento executarPagamento(FormaPagamentoRequest request, BigDecimal valorTotal, Cliente cliente) {
        MetodoPagamento metodoPagamento = request.metodoPagamento();

        Long cartaoId = request.cartaoId();

        Pagamento.PagamentoBuilder pagamentoBuilder = Pagamento.builder().metodoPagamento(metodoPagamento).statusPagamento(StatusPagamento.PENDENTE);

        if (cartaoId != null) {
            var cartao = cartaoService.pegarCartao(cartaoId);

            CartaoPedido cartaoPedido = CartaoPedido.builder()
                    .nomeBanco(cartao.nomeBanco())
                    .numeroCartao(cartao.numeroCartao())
                    .nomeNoCartao(cartao.nomeNoCartao())
                    .validadeCartao(cartao.validadeCartao())
                    .numeroDeParcelas(request.numeroParcelas())
                    .tipoCartao(cartao.tipoCartao()).build();

            pagamentoBuilder.cartaoPedido(cartaoPedido);
        }

        Pagamento pagamento = pagamentoBuilder.build();

        pagamento.getMetodoPagamento().getProcessador(valorTotal, cliente).executa();

        return pagamento;
    }
}
