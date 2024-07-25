package br.com.danielschiavo.order.mapper;

import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.order.dto.request.OrderItemRequest;
import br.com.danielschiavo.order.dto.request.PlaceOrderRequest;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    DetailOrderResponse toShowOrder(Order order);

    Order toEntity(PlaceOrderRequest request);

}
