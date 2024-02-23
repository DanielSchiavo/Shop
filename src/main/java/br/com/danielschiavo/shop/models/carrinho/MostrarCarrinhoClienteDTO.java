package br.com.danielschiavo.shop.models.carrinho;

import java.math.BigDecimal;
import java.util.List;

public record MostrarCarrinhoClienteDTO(
							List<MostrarItemCarrinhoClienteDTO> itemCarrinho,
							BigDecimal valorTotal
								) {

}
