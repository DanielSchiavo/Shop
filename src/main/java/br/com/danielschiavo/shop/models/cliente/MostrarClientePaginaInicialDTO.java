package br.com.danielschiavo.shop.models.cliente;

public record MostrarClientePaginaInicialDTO(
		String nome,
		byte[] fotoPerfil
		) {

	public MostrarClientePaginaInicialDTO(String nome, byte[] fotoPerfil) {
		this.nome = nome;
		this.fotoPerfil = fotoPerfil;
	}

}
