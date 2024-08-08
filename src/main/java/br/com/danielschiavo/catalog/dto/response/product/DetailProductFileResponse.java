package br.com.danielschiavo.catalog.dto.response.product;

import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import br.com.danielschiavo.shared.DetailFileResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailProductFileResponse {
        private Long id;
        private ProductFileType type;
        @JsonProperty("url_video")
        private String urlVideo;
        private Byte position;
        @JsonProperty("file_data")
        private DetailFileResponse fileData;
}
