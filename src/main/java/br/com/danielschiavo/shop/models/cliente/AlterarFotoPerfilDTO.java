package br.com.danielschiavo.shop.models.cliente;

public record AlterarFotoPerfilDTO(String nomeNovaFotoPerfil) {
	
	public AlterarFotoPerfilDTO(String nomeNovaFotoPerfil) {
		this.nomeNovaFotoPerfil = nomeNovaFotoPerfil;
	}

}
