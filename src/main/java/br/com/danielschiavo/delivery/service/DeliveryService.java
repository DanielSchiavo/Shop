package br.com.danielschiavo.delivery.service;

import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.delivery.dto.request.AddDeliveryRequest;
import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.delivery.mapper.DeliveryMapper;
import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import br.com.danielschiavo.delivery.model.valueobject.DeliveryAddress;
import br.com.danielschiavo.delivery.model.entity.Delivery;
import br.com.danielschiavo.delivery.repository.DeliveryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    @Autowired
    private DeliveryMapper mapper;

    public ShowDeliveryResponse executeDelivery(Long deliveryId) {
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new ValidationException("There's no delivery with provided ID " + deliveryId));

        delivery.getDeliveryType().getProcessor().execute(delivery);

        return mapper.toDto(delivery);
    }

    public ShowDeliveryResponse createDelivery(AddDeliveryRequest request, DetailAddressResponse address) {
        boolean needToShip = request.deliveryType() != DeliveryType.DIGITAL && request.deliveryType() != DeliveryType.PICK_UP_IN_STORE;

        if (needToShip && request.addressId() == null) {
            throw new ValidationException("Since the choosed delivery type was " + request.deliveryType() + " you must to give the address that the product(s) will be sent");
        }

        Delivery delivery = new Delivery();
        delivery.setDeliveryType(request.deliveryType());

        if (address != null) {
            DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                    .postalCode(address.postalCode())
                    .street(address.street())
                    .number(address.number())
                    .complement(address.complement())
                    .neighborhood(address.neighborhood())
                    .city(address.city())
                    .state(address.state()).build();

            delivery.setDeliveryAddress(deliveryAddress);
        }

        return mapper.toDto(repository.save(delivery));
    }
}
