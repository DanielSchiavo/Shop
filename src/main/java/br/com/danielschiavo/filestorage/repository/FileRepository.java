package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    Optional<File> findByNameAndBucket(String fileName, Bucket bucket);

    boolean existsByNameAndBucket_name(String fileName, String bucketName);
}
