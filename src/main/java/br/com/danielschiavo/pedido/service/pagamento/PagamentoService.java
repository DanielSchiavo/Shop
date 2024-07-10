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

    public Pagamento executarPagamento(FormaPagamentoRequest request, BigDecimal valorTotal, Cliente cliente) {
        MetodoPagamento metodoPagamento = request.metodoPagamento();

        Long cartaoId = request.cartaoId();

        Pagamento.PagamentoBuilder pagamentoBuilder = Pagamento.builder().metodoPagamento(metodoPagamento).statusPagamento(StatusPagamento.PENDENTE);

        if (cartaoId != null) {
            var cartao = cartaoService.pegarCartao(cartaoId, cliente.getId());

            CartaoPedido cartaoPedido = CartaoPedido.builder()
                    .nomeBanco(cartao.getNomeBanco())
                    .numeroCartao(cartao.getNumeroCartao())
                    .nomeNoCartao(cartao.getNomeNoCartao())
                    .validadeCartao(cartao.getValidadeCartao())
                    .numeroDeParcelas(request.numeroParcelas())
                    .tipoCartao(cartao.getTipoCartao()).build();

            pagamentoBuilder.cartaoPedido(cartaoPedido);
        }

        Pagamento pagamento = pagamentoBuilder.build();

        pagamento.getMetodoPagamento().getProcessador(valorTotal, cliente).executa();

        return pagamento;
    }
}
