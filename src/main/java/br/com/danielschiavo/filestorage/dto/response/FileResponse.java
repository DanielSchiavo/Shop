package br.com.danielschiavo.filestorage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record FileResponse(
		String message,
		@JsonProperty("file_name")
		String fileName,
		@JsonProperty("content_type")
		String contentType,
		@JsonProperty("registration_date_time")
		LocalDateTime registrationDateTime,
		@JsonProperty("last_modified_date_time")
		LocalDateTime lastModifiedDateTime,
		byte[] content) {
}
