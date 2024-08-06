package br.com.danielschiavo.filestorage.infra.cloud;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.net.URL;

@Data
@Validated
@Configuration
@ConfigurationProperties("aws.storage")
public class StorageProperties {

    @Valid
    private S3 s3 = new S3();
    @Valid
    private Image image = new Image();
    @Valid
    private Document document = new Document();

    @Data
    public class S3 {
        @NotBlank
        private String accessKey;
        @NotBlank
        private String secretKey;
        @NotBlank
        private String bucket;
        @NotBlank
        private String region;
    }

    @Data
    public class Image {
        @NotNull
        private URL downloadUrl;
    }

    @Data
    public class Document {
        @NotNull
        private URL downloadUrl;
    }
}