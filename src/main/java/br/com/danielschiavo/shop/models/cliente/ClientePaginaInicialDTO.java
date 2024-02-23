package br.com.danielschiavo.shop.models.cliente;

public record ClientePaginaInicialDTO(
		String nome,
		byte[] fotoPerfil
		) {

	public ClientePaginaInicialDTO(String nome, byte[] fotoPerfil) {
		this.nome = nome;
		this.fotoPerfil = fotoPerfil;
	}

}
