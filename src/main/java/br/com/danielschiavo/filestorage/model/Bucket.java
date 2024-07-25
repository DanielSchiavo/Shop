package br.com.danielschiavo.filestorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "file_storage_buckets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bucket {

    @Id
    private String name;

    private LocalDateTime creationDateTime;

    private Long createdByAdminId;

    @Enumerated(EnumType.STRING)
    private BucketAccessPermission accessPermission;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<File> files;
}
