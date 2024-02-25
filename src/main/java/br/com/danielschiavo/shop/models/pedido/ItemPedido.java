package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
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

	private String nome_produto;
	private String primeira_imagem;
	
	public ItemPedido(Produto produto, ItemCarrinhoDTO itemCarrinhoDTO) {
		this.preco = produto.getPreco();
		this.quantidade = itemCarrinhoDTO.quantidade();
		this.nome_produto = produto.getNome();
		this.primeira_imagem = produto.pegarPrimeiraImagem(produto.getArquivosProduto());
	}
	
}
