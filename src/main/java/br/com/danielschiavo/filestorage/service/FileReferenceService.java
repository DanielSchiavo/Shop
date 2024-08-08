package br.com.danielschiavo.filestorage.service;

import br.com.danielschiavo.filestorage.dto.request.UploadObjectRequest;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.dto.response.ShowGeneratedUrlResponse;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import br.com.danielschiavo.filestorage.infra.cloud.CloudStorageProvider;
import br.com.danielschiavo.filestorage.mapper.FileReferenceMapper;
import br.com.danielschiavo.filestorage.model.FileReference;
import br.com.danielschiavo.filestorage.model.FileReferenceKey;
import br.com.danielschiavo.filestorage.repository.FileReferenceRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@EnableScheduling
public class FileReferenceService {

    @Autowired
    private FileReferenceRepository repository;

    @Autowired
    private FileReferenceMapper mapper;

    @Autowired
    private CloudStorageProvider cloudStorage;

    public ShowGeneratedUrlResponse generateUploadUrl(UploadObjectRequest request) {
        FileReference fileReference = mapper.toEntity(request);
        fileReference.setIsPublicAccessible(true);

        repository.save(fileReference);
        URL presignedUploadUrl = cloudStorage.generatePresignedUploadUrl(fileReference);
        return new ShowGeneratedUrlResponse(fileReference.getFile().getFileName(), fileReference.getFile().getDirectory(), presignedUploadUrl.toString());
    }

    public ShowGeneratedUrlResponse generateDownloadUrl(String filePath, boolean isAdmin) {
        String[] split = filePath.split("/");
        String fileName = split[split.length - 1];
        String directory = filePath.replace("/" + fileName, "");

        FileReference fileReference = repository.findById(new FileReferenceKey(fileName, directory))
                .orElseThrow(() -> new FileStorageException("There is no file with provided path: " + filePath));

        if (!fileReference.getIsPublicAccessible() && !isAdmin) {
            throw new FileStorageException("You can't download this file because it is not public accessible and you're not an administrator");
        }

        URL url = cloudStorage.generatePresignedDownloadUrl(fileReference);
        return new ShowGeneratedUrlResponse(fileName, directory, url.toString());
    }

    public boolean fileExists(String directory, String fileName) {
        return repository.existsById(new FileReferenceKey(fileName, directory));
    }

    public void softDelete(String directory, String fileName) {
        FileReference fileReference = repository.findById(new FileReferenceKey(directory, fileName))
                .orElseThrow(() -> new ValidationException("There is no file with provided directory and name"));

        String destDirectory = "deleted/";
        this.cloudStorage.moveFile(
                fileReference.getFile().getPath(),
                destDirectory + fileName);

        fileReference.getFile().setDirectory(destDirectory + fileReference.getFile().getDirectory());
        repository.save(fileReference);
    }

    @Scheduled(cron = "0 0 0 * * ?")  // This means midnight every day
    @Transactional
    public void removeOldTempFiles() {
        List<FileReference> fileReferences = this.repository.findAllByTempIsTrueAndCreatedAtBefore(
                OffsetDateTime.now().minus(Duration.ofDays(1)));

        for (FileReference fileReference : fileReferences) {
            this.repository.delete(fileReference);
            this.repository.flush();
            try {
                this.cloudStorage.removeFile(fileReference.getFile().getPath());
            } catch (FileStorageException e) {
                log.warn(e.getMessage());
            }
        }

    }

    public FileReferenceKey copy(String sourceDirectory, String sourceFileName, String destDirectory, String destFileName) {
        FileReferenceKey fileReferenceKey = new FileReferenceKey(destDirectory, destFileName);
        if (repository.existsById(fileReferenceKey)) {
            return fileReferenceKey;
        }

        FileReference fileReference = repository.findById(new FileReferenceKey(sourceDirectory, sourceFileName))
                .orElseThrow(() -> new ValidationException("Could not copy object because an object with provided source file path does not exist"));

        FileReference newFileReference = mapper.copyFileReference(fileReference, destDirectory);
        repository.save(newFileReference);

        cloudStorage.copyFile(sourceDirectory + "/" + sourceFileName, destDirectory + "/" + destFileName);
        return fileReferenceKey;
    }

    public void deleteAll(String directory, List<String> fileNames) {
        cloudStorage.removeFiles(directory, fileNames);
        repository.deleteAllById(fileNames.stream().map(name -> new FileReferenceKey(directory, name)).toList());
    }

    public void delete(String directory, String fileName) {
        cloudStorage.removeFile(directory + "/" + fileName);
        repository.deleteById(new FileReferenceKey(directory, fileName));
    }

    public DetailFileReferenceResponse getById(String directory, String fileName) {
        FileReference fileReference = repository.findById(new FileReferenceKey(directory, fileName))
                .orElseThrow(() -> new ValidationException("There is no file with provided id"));
        return mapper.toDtoDetail(fileReference);
    }

    public List<DetailFileReferenceResponse> getAllById(String directory, Collection<String> fileNames) {
        return mapper.toDtoDetail(repository.findAllById(fileNames.stream().map(name -> new FileReferenceKey(directory, name)).toList()));
    }
}
