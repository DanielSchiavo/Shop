package br.com.danielschiavo.delivery.controller;

import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.service.address.AddressService;
import br.com.danielschiavo.delivery.dto.request.AddDeliveryRequest;
import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.delivery.service.DeliveryService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    @Autowired
    private AddressService addressService;

    @Autowired
    private SecurityService securityService;

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody AddDeliveryRequest request) {
        Long customerId = securityService.getCustomerId();

        DetailAddressResponse address = null;
        if (request.addressId() != null) {
            address = addressService.getAddressByIdAndCustomerId(request.addressId(), customerId);
        }

        service.createDelivery(request, address);

        return ResponseEntity.ok().body(Response.success("Delivery created successfully!", null));
    }

    @PostMapping("/{deliveryId}/execute")
    public ResponseEntity<?> executeDelivery(@PathVariable Long deliveryId) {
        ShowDeliveryResponse delivery = service.executeDelivery(deliveryId);

        return ResponseEntity.ok().body(Response.success("Success executing delivery for customer", delivery));
    }
}
