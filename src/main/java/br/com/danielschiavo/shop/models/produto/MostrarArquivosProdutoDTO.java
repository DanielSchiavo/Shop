package br.com.danielschiavo.shop.models.produto;

public record MostrarArquivosProdutoDTO(
		String tipo,
		int tamanho,
		int posicao,
		byte[] imagem
		) {

}
