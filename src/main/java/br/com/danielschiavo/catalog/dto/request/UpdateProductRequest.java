package br.com.danielschiavo.catalog.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UpdateProductRequest(
		String name,
		String description,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		@JsonProperty("category_id")
		Long categoryId,
		Set<AddProductFileRequest> files,
		@JsonProperty("delivery_types")
		Set<DeliveryType> deliveryTypes
		) {

}
