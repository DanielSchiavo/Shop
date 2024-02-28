package br.com.danielschiavo.shop.models.pedido.entrega;

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
