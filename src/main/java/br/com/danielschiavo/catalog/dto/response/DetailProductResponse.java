package br.com.danielschiavo.catalog.dto.response;

import java.math.BigDecimal;
import java.util.List;

import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;

public record DetailProductResponse(
		Long id,
		String name,
		String description,
		BigDecimal price,
		Integer quantity,
		Boolean active,
		Long subCategoryId,
		List<FileInfoResponse> files
){

}
