package br.com.danielschiavo.vendas.dto.request;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ItemCarrinhoRequest(
		@NotNull
		@NumberFormat
		@Positive
		Long produtoId,
		@NotNull
		@NumberFormat
		Integer quantidade
		) {

}
