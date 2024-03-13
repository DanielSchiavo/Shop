package br.com.danielschiavo.shop.models.carrinho;

import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;

public record MostrarItemCarrinhoDTO(
				Long idProduto,
				Integer quantidade
		) {
	
	public static MostrarItemCarrinhoDTO converterItemCarrinhoEmMostrarItemCarrinhoDTO(ItemCarrinho itemCarrinho) {
    	return new MostrarItemCarrinhoDTO(itemCarrinho.getProduto().getId(), itemCarrinho.getQuantidade());
    }
}
