package br.com.danielschiavo.shop.models.pedido;

import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import jakarta.validation.constraints.NotNull;

public record CriarPagamentoDTO(
				@NotNull
				MetodoPagamento metodoPagamento,
				Long idCartao,
				String numeroParcelas
		) {

}
