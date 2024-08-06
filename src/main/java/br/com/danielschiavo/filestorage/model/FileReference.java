package br.com.danielschiavo.filestorage.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "files_references")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileReference {

    @EmbeddedId
    private FileReferenceKey file;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    private String contentType;

    private Long contentLength;

    @Builder.Default
    private Boolean temp = true;

    @Enumerated(EnumType.STRING)
    private FileType type;

    private Boolean isPublicAccessible;
}