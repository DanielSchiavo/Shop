package br.com.danielschiavo.shop.models.pedido.entrega;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record MostrarEntregaDTO(
		TipoEntrega tipoEntrega,
		MostrarEnderecoPedidoDTO endereco
		) {

}
