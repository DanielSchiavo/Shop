package br.com.danielschiavo.filestorage.dto.request;

import br.com.danielschiavo.filestorage.service.validation.AllowedContentTypes;
import br.com.danielschiavo.filestorage.service.validation.AllowedFileExtensions;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadObjectRequest(

        @NotBlank
        @JsonProperty("file_name")
        @AllowedFileExtensions({"png","jpg", "jpeg"})
        String fileName,
        String directory,
        @NotBlank
        @JsonProperty("content_type")
        @AllowedContentTypes({"image/jpg", "image/png", "image/jpeg"})
        String contentType,
        @NotNull
        @JsonProperty("content_length")
        @Min(1)
        Long contentLength
) {
}
