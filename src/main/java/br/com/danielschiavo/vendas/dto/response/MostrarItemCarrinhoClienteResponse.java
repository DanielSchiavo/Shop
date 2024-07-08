package br.com.danielschiavo.vendas.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record MostrarItemCarrinhoClienteResponse(
							Long produtoId,
							Integer quantidade,
							BigDecimal subTotal
		) {

}
