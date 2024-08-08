package br.com.danielschiavo.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.order.model.enums.OrderStatus;
import br.com.danielschiavo.shared.DetailFileResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailOrderResponse {

	private UUID id;

	@JsonProperty("customer_id")
	private Long customerId;

	@JsonProperty("total_value")
	private BigDecimal totalValue;

	@JsonProperty("order_date")
	private LocalDateTime orderDate;

	@JsonProperty("order_status")
	private OrderStatus orderStatus;

	private ShowDeliveryResponse delivery;

	private ShowPaymentResponse payment;

	private List<DetailOrderItemResponse> items;

	public void addPaymentAndDelivery(ShowPaymentResponse payment, ShowDeliveryResponse delivery){
		this.payment = payment;
		this.delivery = delivery;
	}

	public Set<String> getAllFileNames() {
		return items.stream().map(a -> a.firstImage().getFileName()).collect(Collectors.toSet());
	}

	public List<DetailFileResponse> getAllDetailFile() {
		return items.stream().map(DetailOrderItemResponse::firstImage).toList();
	}
}