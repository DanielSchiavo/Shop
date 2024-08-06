package br.com.danielschiavo.catalog.dto.response;

import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailProductFileResponse {
        private Long id;
        @JsonProperty("file_name")
        private String fileName;
        private String directory;
        private ProductFileType type;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("url_video")
        private String urlVideo;
        private Byte position;
        @JsonProperty("content_length")
        private Long contentLength;
        @JsonProperty("download_url")
        private String downloadUrl;
}
