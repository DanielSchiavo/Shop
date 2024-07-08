package br.com.danielschiavo.cliente.dto.request.endereco;

import lombok.Builder;

@Builder
public record AlterarEnderecoRequest(
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
