package br.com.danielschiavo.shared;

import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailFileResponse {

    @JsonProperty("file_name")
    private String fileName;
    private String directory;
    @JsonProperty("content_type")
    private String contentType;
    private Byte position;
    @JsonProperty("content_length")
    private Long contentLength;
    @JsonProperty("download_url")
    private String downloadUrl;
}
