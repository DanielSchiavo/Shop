package br.com.danielschiavo.catalog.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import br.com.danielschiavo.order.model.enums.DeliveryType;
import br.com.danielschiavo.catalog.dto.AddProductFileRequest;
import lombok.Builder;

@Builder
public record UpdateProductRequest(
		String name,
		String description,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		Long subCategoryId,
		Set<AddProductFileRequest> files,
		Set<DeliveryType> deliveryTypes
		) {

}
