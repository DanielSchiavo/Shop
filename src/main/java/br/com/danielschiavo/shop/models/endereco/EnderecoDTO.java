package br.com.danielschiavo.shop.models.endereco;

public record EnderecoDTO(
		String cep,
		String rua,
		String numero,
		String complemento,
		String bairro,
		String cidade,
		String estado,
		Boolean enderecoPadrao
		) {

}
