package br.com.danielschiavo.filestorage.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShowGeneratedUrlResponse(
        @JsonProperty("file_name")
        String fileName,
        String directory,
        String url
) {


}
