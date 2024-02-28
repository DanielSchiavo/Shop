package br.com.danielschiavo.shop.models.cartao;

public record CartaoDTO(
		String validadeCartao,
		String numeroCartao,
		String nomeNoCartao,
		Boolean cartaoPadrao,
		TipoCartao tipoCartao
		) {

}
