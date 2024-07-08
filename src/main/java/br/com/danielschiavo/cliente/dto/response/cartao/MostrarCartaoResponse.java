package br.com.danielschiavo.cliente.dto.response.cartao;

import br.com.danielschiavo.cliente.model.enums.TipoCartao;
import lombok.Builder;

@Builder
public record MostrarCartaoResponse(
			Long id,
			String nomeBanco,
			String numeroCartao,
			String nomeNoCartao,
			String validadeCartao,
			TipoCartao tipoCartao,
			Boolean cartaoPadrao
		) {
}
