package br.com.danielschiavo.pedido.model.entrega;

public record CriarEnderecoPedidoDTO(
			String cep,
			String rua,
			String numero,
			String complemento,
			String bairro,
			String cidade,
			String estado
		) {

}
