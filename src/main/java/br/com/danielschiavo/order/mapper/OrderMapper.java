package br.com.danielschiavo.order.mapper;

import br.com.danielschiavo.order.dto.request.order.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.order.ShowOrderResponse;
import br.com.danielschiavo.order.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    ShowOrderResponse toDto(Order order);

    Order toEntity(PlaceOrderRequest request);
}
