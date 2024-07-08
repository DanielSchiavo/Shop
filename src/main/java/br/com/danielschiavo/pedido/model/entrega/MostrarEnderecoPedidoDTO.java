package br.com.danielschiavo.pedido.model.entrega;

public record MostrarEnderecoPedidoDTO(
        String cep,
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {

    public MostrarEnderecoPedidoDTO(EnderecoPedido enderecoPedido) {
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
