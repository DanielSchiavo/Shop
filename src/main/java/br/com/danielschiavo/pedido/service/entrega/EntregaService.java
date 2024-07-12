package br.com.danielschiavo.pedido.service.entrega;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.endereco.EnderecoService;
import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.pedido.model.valueobject.EnderecoPedido;
import br.com.danielschiavo.pedido.model.entity.Entrega;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private EnderecoService enderecoService;

    public Entrega executarEntrega(Entrega entrega, Cliente cliente) {
        boolean NaoEhEntregaDigital = entrega.getTipoEntrega() != TipoEntrega.ENTREGA_DIGITAL;

        if (NaoEhEntregaDigital) {
            var endereco = enderecoService.pegarEnderecoPorId(entrega.getEnderecoPedido().getEnderecoId(), cliente.getId());
            EnderecoPedido enderecoPedido = EnderecoPedido.builder()
                    .cep(endereco.getCep())
                    .rua(endereco.getRua())
                    .numero(endereco.getNumero())
                    .complemento(endereco.getComplemento())
                    .bairro(endereco.getBairro())
                    .cidade(endereco.getCidade())
                    .estado(endereco.getEstado()).build();

            entrega.setEnderecoPedido(enderecoPedido);
        }

        entrega.getTipoEntrega().getProcessador().executa(cliente);

        return entrega;
    }
}
