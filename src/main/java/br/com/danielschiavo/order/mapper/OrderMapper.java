package br.com.danielschiavo.order.mapper;

import br.com.danielschiavo.order.dto.request.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    DetailOrderResponse toShowOrder(Order order);

    Order toEntity(PlaceOrderRequest request);

}
