package br.com.danielschiavo.pedido.service.user;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.EnderecoService;
import br.com.danielschiavo.pedido.model.entrega.EnderecoPedido;
import br.com.danielschiavo.pedido.model.entrega.Entrega;
import br.com.danielschiavo.pedido.model.entrega.FormaEntregaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private EnderecoService enderecoService;

    public Entrega executarEntrega(FormaEntregaRequest request, Cliente cliente) {
        Entrega.EntregaBuilder entregaBuilder = Entrega.builder().tipoEntrega(request.tipoEntrega());

        if (request.enderecoId() != null) {
            var endereco = enderecoService.pegarEnderecoPorId(request.enderecoId());
            EnderecoPedido enderecoPedido = EnderecoPedido.builder()
                    .cep(endereco.getCep())
                    .rua(endereco.getRua())
                    .numero(endereco.getNumero())
                    .complemento(endereco.getComplemento())
                    .bairro(endereco.getBairro())
                    .cidade(endereco.getCidade())
                    .estado(endereco.getEstado()).build();

            entregaBuilder.enderecoPedido(enderecoPedido);
        }

        Entrega entrega = entregaBuilder.build();
        entrega.getTipoEntrega().getProcessador(cliente).executa();

        return entrega;
    }
}
