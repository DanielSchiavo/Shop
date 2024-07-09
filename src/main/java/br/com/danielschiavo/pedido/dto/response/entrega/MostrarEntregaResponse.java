package br.com.danielschiavo.pedido.dto.response.entrega;

import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarEntregaResponse(
		TipoEntrega tipoEntrega,
		MostrarEnderecoPedidoResponse endereco
		) {

}
