package br.com.danielschiavo.pedido.dto.request.pagamento;

import br.com.danielschiavo.pedido.model.enums.MetodoPagamento;
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
