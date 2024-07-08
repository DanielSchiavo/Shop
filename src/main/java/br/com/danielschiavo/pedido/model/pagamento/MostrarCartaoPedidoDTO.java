package br.com.danielschiavo.pedido.model.pagamento;

import br.com.danielschiavo.cliente.model.enums.TipoCartao;

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
