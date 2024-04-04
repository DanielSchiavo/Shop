package br.com.danielschiavo.shop.models.produto.dto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.produto.categoria.MostrarCategoriaComSubCategoriaDTO;
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
public class MostrarProdutosDTO {

	private Long id;
	private String nome;
	private BigDecimal preco;
	private Integer quantidade;
	private Boolean ativo;
	private MostrarCategoriaComSubCategoriaDTO categoria;
	private byte[] primeiraImagem;

}
