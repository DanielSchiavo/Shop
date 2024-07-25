package br.com.danielschiavo.catalog.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record RegisterProductRequest(
		@NotBlank
		String name,
		@NotBlank
		String description,
		@NotNull
		@Positive
		BigDecimal price,
		@NotNull
		Integer quantity,
		@NotNull
		Boolean active,
		@NotNull
		Long categoryId,
		@NotNull
		@JsonProperty("delivery_types")
		Set<DeliveryType> deliveryTypes,
		@NotNull
		Set<AddProductFileRequest> files
		) {	


}
