package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.dto.request.CreateBucketRequest;
import br.com.danielschiavo.filestorage.dto.response.UpdateBucketRequest;
import br.com.danielschiavo.filestorage.mapper.BucketMapper;
import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.repository.BucketRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class BucketService {

    @Autowired
    private BucketRepository repository;

    @Autowired
    private BucketMapper mapper;

    private final Path root = Paths.get("filestorage");

    public Bucket createBucket(Long customerId, CreateBucketRequest request) {
        Bucket createBucket = mapper.toEntity(request, customerId);
        Path bucketDirectory = root.resolve(createBucket.getName());

        createBucket.setCreatedByAdminId(customerId);
        createBucket.setPath(bucketDirectory.toString());
        try {
            if (!Files.exists(bucketDirectory)) {
                Files.createDirectories(bucketDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return repository.save(createBucket);
    }

    public void deleteBucket(Long bucketId) {
        Bucket bucket = getBucketById(bucketId);

        Path bucketDirectory = root.resolve(bucket.getName());
        deleteDirectoryAndAllFiles(bucketDirectory);

        repository.delete(bucket);
    }

    public Bucket getBucketById(Long bucketId) {
        return repository.findById(bucketId).orElseThrow(() -> new ValidationException("There's no bucket with the given id"));
    }

    public void updateBucket(Long bucketId, UpdateBucketRequest request) {
        Bucket bucket = getBucketById(bucketId);

        Path oldBucketDirectory = root.resolve(bucket.getName());
        Path newBucketDirectory = root.resolve(request.bucketName());

        bucket.setName(request.bucketName());
        try {
            Files.move(oldBucketDirectory, newBucketDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectoryAndAllFiles(Path path){
        CompletableFuture<Void> allFilesDeletedFuture = CompletableFuture.runAsync(() -> {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            Files.delete(entry);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, Executors.newFixedThreadPool(3));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        allFilesDeletedFuture.thenRun(() -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Bucket getBucketByName(String bucketName) {
        return repository.findByBucketName(bucketName).orElseThrow(() -> new ValidationException("There's no bucket with name: " + bucketName));
    }
}
