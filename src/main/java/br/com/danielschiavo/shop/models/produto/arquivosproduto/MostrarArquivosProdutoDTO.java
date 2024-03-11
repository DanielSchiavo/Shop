package br.com.danielschiavo.shop.models.produto.arquivosproduto;

public record MostrarArquivosProdutoDTO(
		String tipo,
		int tamanho,
		int posicao,
		byte[] imagem
		) {

}
