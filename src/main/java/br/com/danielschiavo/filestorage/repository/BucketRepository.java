package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    Optional<Bucket> findByName(String bucketName);
}
