package br.com.danielschiavo.shop.models.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AlterarFotoPerfilDTO(
	    @NotBlank
	    @Pattern(regexp = ".*\\..*")
		String nomeNovaFotoPerfil
		) {
	
	public AlterarFotoPerfilDTO(String nomeNovaFotoPerfil) {
		this.nomeNovaFotoPerfil = nomeNovaFotoPerfil;
	}

}
