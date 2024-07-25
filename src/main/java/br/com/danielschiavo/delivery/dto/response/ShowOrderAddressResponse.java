package br.com.danielschiavo.delivery.dto.response;

import br.com.danielschiavo.delivery.model.valueobject.DeliveryAddress;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShowOrderAddressResponse(
        @JsonProperty("postal_code")
        String postalCode,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state
) {

    public ShowOrderAddressResponse(DeliveryAddress deliveryAddress) {
        this(
                deliveryAddress.getPostalCode(),
                deliveryAddress.getStreet(),
                deliveryAddress.getNumber(),
                deliveryAddress.getComplement(),
                deliveryAddress.getNeighborhood(),
                deliveryAddress.getCity(),
                deliveryAddress.getState()
        );
    }
}
