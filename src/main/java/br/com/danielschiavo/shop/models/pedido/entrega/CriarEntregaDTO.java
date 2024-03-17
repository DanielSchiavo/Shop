package br.com.danielschiavo.shop.models.pedido.entrega;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import jakarta.validation.constraints.NotNull;

public record CriarEntregaDTO(
			@NotNull
			TipoEntrega tipoEntrega,
			Long idEndereco
		) {

}
