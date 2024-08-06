package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.FileReference;
import br.com.danielschiavo.filestorage.model.FileReferenceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface FileReferenceRepository extends JpaRepository<FileReference, FileReferenceKey> {
    List<FileReference> findAllByTempIsTrueAndCreatedAtBefore(OffsetDateTime minus);
}
