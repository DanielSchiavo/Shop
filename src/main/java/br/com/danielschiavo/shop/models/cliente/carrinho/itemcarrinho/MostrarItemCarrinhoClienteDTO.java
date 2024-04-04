package br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record MostrarItemCarrinhoClienteDTO(
							Long idProduto,
							String nomeProduto,
							Integer quantidade,
							BigDecimal preco,
							byte[] imagemProduto
		) {

}
