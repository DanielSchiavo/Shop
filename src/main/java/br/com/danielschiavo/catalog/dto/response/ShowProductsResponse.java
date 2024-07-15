package br.com.danielschiavo.produto.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowProductsResponse {

	private Long id;
	private String nome;
	private BigDecimal preco;
	private Integer quantidade;
	private Boolean ativo;
	private String primeiraImagem;

}
