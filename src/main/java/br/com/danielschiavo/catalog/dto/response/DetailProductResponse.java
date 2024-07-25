package br.com.danielschiavo.catalog.dto.response;

import java.math.BigDecimal;
import java.util.List;

import br.com.danielschiavo.filestorage.dto.response.FileResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailProductResponse(
		Long id,
		String name,
		String description,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		@JsonProperty("category_id")
		Long categoryId,
		List<FileResponse> files
){

}
