package br.com.danielschiavo.catalog.dto.response;

import br.com.danielschiavo.delivery.dto.response.ShowDeliveryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailProductResponse {

	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer quantity;
	private Boolean active;
	@JsonProperty("category_id")
	private Long categoryId;
	private List<DetailProductFileResponse> files;
	@JsonProperty("deliveries_types")
	private List<ShowDeliveryResponse> deliveryTypes;

}
