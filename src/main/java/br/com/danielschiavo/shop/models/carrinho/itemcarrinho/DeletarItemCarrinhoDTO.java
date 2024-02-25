package br.com.danielschiavo.shop.models.carrinho.itemcarrinho;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeletarItemCarrinhoDTO(
		@NotNull
		@NumberFormat
		@Positive
		Long produtoId
		) {

}
