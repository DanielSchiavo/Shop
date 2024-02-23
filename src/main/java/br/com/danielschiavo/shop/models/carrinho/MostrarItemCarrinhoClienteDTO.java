package br.com.danielschiavo.shop.models.carrinho;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.produto.Produto;

public record MostrarItemCarrinhoClienteDTO(
							String nomeProduto,
							Integer quantidade,
							BigDecimal preco,
							byte[] imagemProduto
		) {

	
	
    public MostrarItemCarrinhoClienteDTO(Produto produto, byte[] imagemProduto, Integer quantidade) {
        this(produto.getNome(), quantidade, produto.getPreco(), imagemProduto);
    }


}
