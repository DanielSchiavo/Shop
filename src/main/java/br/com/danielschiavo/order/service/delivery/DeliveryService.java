package br.com.danielschiavo.pedido.service.delivery;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.address.AddressService;
import br.com.danielschiavo.pedido.model.enums.DeliveryType;
import br.com.danielschiavo.pedido.model.valueobject.OrderAddress;
import br.com.danielschiavo.pedido.model.entity.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    private AddressService addressService;

    public Delivery execute(Delivery delivery, Customer customer) {
        boolean doesNotNeedToShip = delivery.getDeliveryType() != DeliveryType.DIGITAL && delivery.getDeliveryType() != DeliveryType.PICK_UP_IN_STORE;

        if (doesNotNeedToShip) {
            var address = addressService.getAddressByIdAndCustomerId(delivery.getOrderAddress().getAddressId(), customer.getId());
            OrderAddress orderAddress = OrderAddress.builder()
                    .postalCode(address.getPostalCode())
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .complement(address.getComplement())
                    .neighborhood(address.getNeighborhood())
                    .city(address.getCity())
                    .state(address.getState()).build();

            delivery.setOrderAddress(orderAddress);
        }

        delivery.getDeliveryType().getProcessor().execute(customer);

        return delivery;
    }
}
