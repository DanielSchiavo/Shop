package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFileNameAndBucket(String fileName, Bucket bucket);
}
