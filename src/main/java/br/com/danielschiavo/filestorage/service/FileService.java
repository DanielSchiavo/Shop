package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.repository.FileRepository;
import br.com.danielschiavo.filestorage.repository.LocalStorageRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private LocalStorageRepository localStorage;

    @Autowired
    private FileRepository repository;

    @Autowired
    private BucketService bucketService;

    public File getFile(String fileName, String bucketName) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        File file = repository.findByFileNameAndBucket(fileName, bucket).orElseThrow(() -> new ValidationException("There's no file with given name in bucket " + bucketName));

        byte[] content = localStorage.get(Path.of(bucket.getPath()), file.getFileName());
        file.setContent(content);

        return file;
    }

    public File registerFile(String bucketName, String fileName, byte[] content, String contentType) {
        if (fileName == null) {
            String extensao = contentType.split("/")[1];
            fileName = UUID.randomUUID() + "." + extensao;
        }

        Bucket bucket = bucketService.getBucketByName(bucketName);

        File file = new File(fileName, content, contentType, LocalDateTime.now(), null, bucket);
        repository.save(file);

        localStorage.save(Path.of(bucket.getPath()), file.getFileName(), content);

        return file;
    }

    public void deleteFile(String bucketName, String fileName) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        File file = getFile(fileName, bucketName);
        repository.delete(file);

        localStorage.delete(Path.of(bucket.getPath()), file.getFileName());
    }

    public File copyFile(String destBucketName, String destFileName, String sourceBucketName, String sourceFileName) {
        Bucket destBucket = bucketService.getBucketByName(destBucketName);
        Bucket sourceBucket = bucketService.getBucketByName(sourceBucketName);
        File sourceFile = getFile(sourceFileName, sourceBucket.getName());

        return registerFile(destBucket.getName(),
                            destFileName == null ? sourceFile.getFileName() : destFileName,
                            sourceFile.getContent(),
                            sourceFile.getContentType());
    }

    public boolean checkIfFileExists(String bucketName, String fileName) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        return repository.findByFileNameAndBucket(fileName, bucket).isPresent();
    }
}
