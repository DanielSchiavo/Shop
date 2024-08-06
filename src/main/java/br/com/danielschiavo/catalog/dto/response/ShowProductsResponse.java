package br.com.danielschiavo.catalog.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowProductsResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private Integer quantity;
	private Boolean active;
	@JsonProperty("first_image")
	private DetailProductFileResponse firstImage;

}
