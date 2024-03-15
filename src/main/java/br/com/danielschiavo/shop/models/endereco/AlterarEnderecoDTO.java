package br.com.danielschiavo.shop.models.endereco;

public record AlterarEnderecoDTO(
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
