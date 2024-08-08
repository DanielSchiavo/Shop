package br.com.danielschiavo.catalog.dto.request.product;

import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddProductFileRequest(
		ProductFileType type,
		@JsonProperty("file_reference_id")
		String fileReferenceId,
		@JsonProperty("url_video")
		String urlVideo,
		@NotNull
		Byte position) {

}
