package br.com.danielschiavo.filestorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_storage_buckets_files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    private String fileName;

    private String prefix;

    private byte[] content;

    private String contentType;

    private LocalDateTime registrationDateTime;

    private LocalDateTime lastModifiedDateTime;

    @ManyToOne
    private Bucket bucket;
}
