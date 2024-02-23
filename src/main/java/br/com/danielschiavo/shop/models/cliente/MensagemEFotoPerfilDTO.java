package br.com.danielschiavo.shop.models.cliente;

public record MensagemEFotoPerfilDTO(String mensagem, byte[] imagem) {
	
	public MensagemEFotoPerfilDTO(String mensagem, byte[] imagem) {
		this.mensagem = mensagem;
		this.imagem = imagem;
	}

}
