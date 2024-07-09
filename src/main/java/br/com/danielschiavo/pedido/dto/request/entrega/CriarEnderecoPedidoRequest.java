package br.com.danielschiavo.pedido.dto.request.entrega;

public record CriarEnderecoPedidoRequest(
			String cep,
			String rua,
			String numero,
			String complemento,
			String bairro,
			String cidade,
			String estado
		) {

}
