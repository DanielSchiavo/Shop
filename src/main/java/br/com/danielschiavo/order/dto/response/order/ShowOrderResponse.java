package br.com.danielschiavo.pedido.dto.response.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.danielschiavo.pedido.dto.response.delivery.ShowOrderDeliveryResponse;
import br.com.danielschiavo.pedido.dto.response.orderitem.ShowOrderItemResponse;
import br.com.danielschiavo.pedido.dto.response.payment.ShowOrderPaymentResponse;
import br.com.danielschiavo.pedido.model.entity.Order;
import br.com.danielschiavo.pedido.model.enums.OrderStatus;
import br.com.danielschiavo.pedido.dto.response.payment.ShowOrderCardResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.pedido.dto.response.delivery.ShowOrderAddressResponse;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ShowOrderResponse(
		UUID orderId,
		Long customerId,
        BigDecimal totalValue,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        ShowOrderDeliveryResponse delivery,
        ShowOrderPaymentResponse payment,
        List<ShowOrderItemResponse> products
) {

    public ShowOrderResponse(Order order, List<ShowOrderItemResponse> listShowOrderItem) {
        this(order.getId(),
        	 order.getCustomerId(),
             order.getTotalValue(),
             order.getOrderDate(),
             order.getOrderStatus(),
             new ShowOrderDeliveryResponse(order.getDelivery().getDeliveryType(),
            		 Optional.ofNullable(order.getDelivery().getOrderAddress())
            		 .map(ShowOrderAddressResponse::new)
            		 .orElse(null)),
             new ShowOrderPaymentResponse(order.getPayment().getPaymentMethod(),
				                	 order.getPayment().getPaymentStatus(),
				                	 Optional.ofNullable(order.getPayment().getOrderCard())
				                	 .map(cartaoPedido -> new ShowOrderCardResponse(order.getPayment().getOrderCard()))
				                	 .orElse(null)),
             listShowOrderItem);
    }

}