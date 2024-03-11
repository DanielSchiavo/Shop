package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaComSubCategoriaDTO;

public record MostrarProdutosDTO(
		Long id, 
		String nome, 
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		MostrarCategoriaComSubCategoriaDTO categoria,
		byte[] primeiraImagem
		) {

	public MostrarProdutosDTO(Produto produto, byte[] primeiraImagem) {
		this(produto.getId(), 
			 produto.getNome(), 
			 produto.getPreco(), 
			 produto.getQuantidade(), 
			 produto.getAtivo(),
			 new MostrarCategoriaComSubCategoriaDTO(produto.getSubCategoria(), produto.getCategoria()),
			 primeiraImagem);
	}


}
