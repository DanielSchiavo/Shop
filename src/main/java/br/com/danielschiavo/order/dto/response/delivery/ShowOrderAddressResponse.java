package br.com.danielschiavo.pedido.dto.response.delivery;

import br.com.danielschiavo.pedido.model.valueobject.OrderAddress;

public record ShowOrderAddressResponse(
        String postalCode,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state
) {

    public ShowOrderAddressResponse(OrderAddress orderAddress) {
        this(
                orderAddress.getPostalCode(),
                orderAddress.getStreet(),
                orderAddress.getNumber(),
                orderAddress.getComplement(),
                orderAddress.getNeighborhood(),
                orderAddress.getCity(),
                orderAddress.getState()
        );
    }
}
