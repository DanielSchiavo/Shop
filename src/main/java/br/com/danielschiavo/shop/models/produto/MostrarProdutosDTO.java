package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaComCategoriaDTO;

public record MostrarProdutosDTO(
		Long id, 
		String nome, 
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		MostrarSubCategoriaComCategoriaDTO subCategoria,
		byte[] primeiraImagem
		) {

	public MostrarProdutosDTO(Produto produto, byte[] primeiraImagem) {
		this(produto.getId(), 
			 produto.getNome(), 
			 produto.getPreco(), 
			 produto.getQuantidade(), 
			 produto.getAtivo(),
			 new MostrarSubCategoriaComCategoriaDTO(produto.getSubCategoria()),
			 primeiraImagem);
	}


}
