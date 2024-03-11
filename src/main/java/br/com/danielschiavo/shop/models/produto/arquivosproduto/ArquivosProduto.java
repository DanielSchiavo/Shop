package br.com.danielschiavo.shop.models.produto.arquivosproduto;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArquivosProduto {
	
	@Column
	private String nome;
	
	@Column
	private Integer posicao;

}
