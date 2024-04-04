package br.com.danielschiavo.shop.models.cliente.carrinho;

import java.math.BigDecimal;
import java.util.List;

import br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho.MostrarItemCarrinhoClienteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MostrarCarrinhoClienteDTO {

	private Long id;
	private List<MostrarItemCarrinhoClienteDTO> itemsCarrinho;
	private BigDecimal valorTotal;
}
