package br.com.danielschiavo.pedido.mapper;

import br.com.danielschiavo.pedido.dto.request.order.PlaceOrderRequest;
import br.com.danielschiavo.pedido.dto.response.order.ShowOrderResponse;
import br.com.danielschiavo.pedido.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    ShowOrderResponse toDto(Order order);

    Order toEntity(PlaceOrderRequest request);
}
