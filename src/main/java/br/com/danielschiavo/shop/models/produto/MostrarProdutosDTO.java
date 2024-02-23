package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;

public record MostrarProdutosDTO(
		Long id, 
		String nome, 
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		byte[] primeiraImagem
		) {

	public MostrarProdutosDTO(Produto produto, byte[] primeiraImagem) {
		this(produto.getId(), produto.getNome(), produto.getPreco(), produto.getQuantidade(), produto.getAtivo(), primeiraImagem);
	}


}
