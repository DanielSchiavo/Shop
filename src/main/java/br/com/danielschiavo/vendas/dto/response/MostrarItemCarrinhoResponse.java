package br.com.danielschiavo.vendas.dto.response;

public record MostrarItemCarrinhoResponse(
				Long idProduto,
				Integer quantidade
		) {
}
