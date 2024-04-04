package br.com.danielschiavo.shop.models.pedido.pagamento;

import br.com.danielschiavo.shop.models.cliente.cartao.TipoCartao;

public record MostrarCartaoPedidoDTO(
		String nomeBanco,
		String numeroCartao,
		String nomeNoCartao,
		String numeroDeParcelas,
		TipoCartao tipoCartao
		) {
	
    public MostrarCartaoPedidoDTO(CartaoPedido dadosCartao) {
        this(
        		dadosCartao.getNomeBanco(),
        		dadosCartao.getNumeroCartao(),
        		dadosCartao.getNomeNoCartao(),
        		dadosCartao.getNumeroDeParcelas(),
        		dadosCartao.getTipoCartao()
        );
    }
}
