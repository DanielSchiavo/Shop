package br.com.danielschiavo.shop.models.pedido.entrega;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;

public record CriarEntregaDTO(
			TipoEntrega tipoEntrega,
			Long idEndereco
		) {

}
