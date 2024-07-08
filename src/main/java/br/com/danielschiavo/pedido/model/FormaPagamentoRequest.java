package br.com.danielschiavo.pedido.model;

import br.com.danielschiavo.pedido.model.pagamento.MetodoPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FormaPagamentoRequest(
				@NotNull
				MetodoPagamento metodoPagamento,
				Long cartaoId,
				String numeroParcelas
		) {

}
