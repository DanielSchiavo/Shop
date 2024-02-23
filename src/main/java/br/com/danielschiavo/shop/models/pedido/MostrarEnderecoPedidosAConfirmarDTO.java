package br.com.danielschiavo.shop.models.pedido;

public record MostrarEnderecoPedidosAConfirmarDTO(
        String cep,
        String rua,
        String numero,
        String complemento,
        String bairro,
        String estado
) {

    public MostrarEnderecoPedidosAConfirmarDTO(EnderecoPedido endereco_pedido) {
        this(
                endereco_pedido.getCep(),
                endereco_pedido.getRua(),
                endereco_pedido.getNumero(),
                endereco_pedido.getComplemento(),
                endereco_pedido.getBairro(),
                endereco_pedido.getEstado()
        );
    }
}
