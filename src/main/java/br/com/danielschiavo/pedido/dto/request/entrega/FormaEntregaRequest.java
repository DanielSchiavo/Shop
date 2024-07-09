package br.com.danielschiavo.pedido.dto.request.entrega;

import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FormaEntregaRequest (
			@NotNull
			TipoEntrega tipoEntrega,
			Long enderecoId
		) {

}
