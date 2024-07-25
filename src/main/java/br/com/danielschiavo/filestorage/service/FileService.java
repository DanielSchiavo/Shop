package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.dto.response.FileResponse;
import br.com.danielschiavo.filestorage.mapper.FileMapper;
import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.repository.FileRepository;
import br.com.danielschiavo.filestorage.repository.LocalStorageRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private LocalStorageRepository localStorage;

    @Autowired
    private FileRepository dbRepository;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private FileMapper mapper;

    public FileResponse getFile(String fileName, String bucketName) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        File file = dbRepository.findByFileNameAndBucket(fileName, bucket).orElseThrow(() -> new ValidationException("There's no file with given name in bucket " + bucketName));

        byte[] content = localStorage.get(Path.of(bucketName), file.getFileName());
        file.setContent(content);

        return mapper.toDto(file);
    }

    public FileResponse registerFile(String bucketName, String fileName, byte[] content, String contentType) {
        if (fileName == null) {
            String extensao = contentType.split("/")[1];
            fileName = UUID.randomUUID() + "." + extensao;
        }

        Bucket bucket = bucketService.getBucketByName(bucketName);

        File file = new File(fileName, null, content, contentType, LocalDateTime.now(), null, bucket);
        dbRepository.save(file);

        localStorage.save(Path.of(bucketName), file.getFileName(), content);

        return mapper.toDto(file);
    }

    public void deleteFile(String bucketName, String fileName) {
        if (!dbRepository.existsByIdAndBucket_name(fileName, bucketName)) {
            throw new ValidationException("Cannot delete because a file with name " + fileName + " does not exist");
        }
        dbRepository.deleteById(fileName);

        localStorage.delete(Path.of(bucketName), fileName);
    }

    public FileResponse copyFile(String destBucketName, String destFileName, String sourceBucketName, String sourceFileName) {
        Bucket destBucket = bucketService.getBucketByName(destBucketName);
        Bucket sourceBucket = bucketService.getBucketByName(sourceBucketName);
        FileResponse sourceFile = getFile(sourceFileName, sourceBucket.getName());

        return registerFile(destBucket.getName(),
                            destFileName == null ? sourceFile.fileName() : destFileName,
                            sourceFile.content(),
                            sourceFile.contentType());
    }

    public boolean checkIfFileExists(String bucketName, String fileName) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        return dbRepository.findByFileNameAndBucket(fileName, bucket).isPresent();
    }
}
