package br.com.danielschiavo.pedido.model.entrega;

import br.com.danielschiavo.pedido.model.TipoEntrega;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FormaEntregaRequest (
			@NotNull
			TipoEntrega tipoEntrega,
			Long enderecoId
		) {

}
