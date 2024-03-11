package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.List;

import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaComSubCategoriaDTO;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.MostrarArquivosProdutoDTO;

public record DetalharProdutoDTO(
		Long id, 
		String nome, 
		String descricao,
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		MostrarCategoriaComSubCategoriaDTO categoria,
		List<MostrarArquivosProdutoDTO> arquivos
		) {
	
	public DetalharProdutoDTO(Produto produto, List<MostrarArquivosProdutoDTO> arquivos) {
		this(
			produto.getId(),
			produto.getNome(),
			produto.getDescricao(),
			produto.getPreco(),
			produto.getQuantidade(),
			produto.getAtivo(),
			new MostrarCategoriaComSubCategoriaDTO(produto.getSubCategoria(), produto.getCategoria()),
			arquivos
			);
	}
	
}
