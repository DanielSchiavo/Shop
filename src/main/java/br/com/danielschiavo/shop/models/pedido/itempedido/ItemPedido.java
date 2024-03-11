package br.com.danielschiavo.shop.models.pedido.itempedido;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.produto.Produto;
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
public class ItemPedido {
	
	private BigDecimal preco;
	private Integer quantidade;
	
	private Long idProduto;
	
	private String nomeProduto;
	private String primeiraImagem;
	
	public ItemPedido(Produto produto, Integer quantidade, String nomePrimeiraImagem) {
		this.preco = produto.getPreco();
		this.quantidade = quantidade;
		this.idProduto = produto.getId();
		this.nomeProduto = produto.getNome();
		this.primeiraImagem = produto.pegarNomePrimeiraImagem();
		this.primeiraImagem = nomePrimeiraImagem;
	}


}
