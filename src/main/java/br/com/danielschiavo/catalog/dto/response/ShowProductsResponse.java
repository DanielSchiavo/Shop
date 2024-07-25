package br.com.danielschiavo.catalog.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public record ShowProductsResponse (
		Long id,
		String name,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		@JsonProperty("first_image")
		String firstImage
) {

}
