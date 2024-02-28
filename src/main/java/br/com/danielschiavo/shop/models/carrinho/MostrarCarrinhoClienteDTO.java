package br.com.danielschiavo.shop.models.carrinho;

import java.math.BigDecimal;
import java.util.List;

public record MostrarCarrinhoClienteDTO(
							Long id,
							List<MostrarItemCarrinhoClienteDTO> itemCarrinho,
							BigDecimal valorTotal
								) {

}
