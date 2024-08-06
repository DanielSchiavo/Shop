package br.com.danielschiavo.filestorage.dto.response;

import br.com.danielschiavo.filestorage.model.FileType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailFileReferenceResponse(
        @JsonProperty("file_name")
        String fileName,
        String directory,
        @JsonProperty("content_type")
        String contentType,
        @JsonProperty("content_length")
        Long contentLength,
        Boolean temp,
        FileType type,
        @JsonProperty("is_public_accessible")
        Boolean isPublicAccessible
) {
}
