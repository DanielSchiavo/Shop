package br.com.danielschiavo.shop.models.cartao;


public record MostrarCartaoDTO(
			Long id,
			String nomeBanco,
			String numeroCartao,
			TipoCartao tipoCartao,
			Boolean cartaoPadrao
		) {

	public MostrarCartaoDTO(Cartao novoCartao) {
		this(novoCartao.getId(),
			 novoCartao.getNomeBanco(),
			 novoCartao.getNumeroCartao(),
			 novoCartao.getTipoCartao(),
			 novoCartao.getCartaoPadrao());
	}
	
	public static MostrarCartaoDTO converterParaMostrarCartaoDTO(Cartao cartao) {
		MostrarCartaoDTO mostrarCartaoDTO = new MostrarCartaoDTO(cartao.getId(),
																 cartao.getNomeBanco(),
																 cartao.getNumeroCartao(),
																 cartao.getTipoCartao(),
																 cartao.getCartaoPadrao());
		return mostrarCartaoDTO;
	}

}
