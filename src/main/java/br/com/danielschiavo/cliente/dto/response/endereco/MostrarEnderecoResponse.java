package br.com.danielschiavo.cliente.dto.response.endereco;

import lombok.Builder;

@Builder
public record MostrarEnderecoResponse(
		Long id,
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
