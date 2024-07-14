package br.com.danielschiavo.pedido.service.entrega;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.endereco.AddressService;
import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.pedido.model.valueobject.EnderecoPedido;
import br.com.danielschiavo.pedido.model.entity.Entrega;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private AddressService enderecoService;

    public Entrega executarEntrega(Entrega entrega, Customer customer) {
        boolean NaoEhEntregaDigital = entrega.getTipoEntrega() != TipoEntrega.ENTREGA_DIGITAL;

        if (NaoEhEntregaDigital) {
            var endereco = enderecoService.getAddressByIdAndCustomerId(entrega.getEnderecoPedido().getEnderecoId(), customer.getId());
            EnderecoPedido enderecoPedido = EnderecoPedido.builder()
                    .cep(endereco.getPostalCode())
                    .rua(endereco.getStreet())
                    .numero(endereco.getNumber())
                    .complemento(endereco.getComplement())
                    .bairro(endereco.getNeighborhood())
                    .cidade(endereco.getCity())
                    .estado(endereco.getState()).build();

            entrega.setEnderecoPedido(enderecoPedido);
        }

        entrega.getTipoEntrega().getProcessador().executa(customer);

        return entrega;
    }
}
