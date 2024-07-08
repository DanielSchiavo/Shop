package br.com.danielschiavo.pedido.model.entrega;

import br.com.danielschiavo.pedido.model.TipoEntrega;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarEntregaDTO(
		TipoEntrega tipoEntrega,
		MostrarEnderecoPedidoDTO endereco
		) {

}
