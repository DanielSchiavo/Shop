package br.com.danielschiavo.catalog.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddProductFileRequest(
		@NotNull
		String name,
		@NotNull
		Byte position) {

}
