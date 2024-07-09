package br.com.danielschiavo.pedido.dto.response.entrega;

import br.com.danielschiavo.pedido.model.valueobject.EnderecoPedido;

public record MostrarEnderecoPedidoResponse(
        String cep,
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {

    public MostrarEnderecoPedidoResponse(EnderecoPedido enderecoPedido) {
        this(
                enderecoPedido.getCep(),
                enderecoPedido.getRua(),
                enderecoPedido.getNumero(),
                enderecoPedido.getComplemento(),
                enderecoPedido.getBairro(),
                enderecoPedido.getCidade(),
                enderecoPedido.getEstado()
        );
    }
}
