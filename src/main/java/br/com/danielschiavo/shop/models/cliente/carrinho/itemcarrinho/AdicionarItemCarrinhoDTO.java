package br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AdicionarItemCarrinhoDTO(
		@NotNull
		@NumberFormat
		@Positive
		Long idProduto,
		@NotNull
		@NumberFormat
		Integer quantidade
		) {

}
