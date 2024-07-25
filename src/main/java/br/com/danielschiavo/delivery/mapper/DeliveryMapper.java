package br.com.danielschiavo.delivery.mapper;

import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.delivery.model.entity.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    ShowDeliveryResponse toDto(Delivery delivery);
}
