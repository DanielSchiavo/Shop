package br.com.danielschiavo.vendas.dto.response;

import java.math.BigDecimal;
import java.util.List;

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
public class MostrarCarrinhoClienteResponse {

	private Long clienteId;
	
	private List<MostrarItemCarrinhoClienteResponse> itemsCarrinho;
	
	private BigDecimal valorTotal;
}
