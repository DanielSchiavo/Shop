package br.com.danielschiavo.filestorage.infra.cloud;

import br.com.danielschiavo.filestorage.model.FileReference;

import java.net.URL;
import java.util.List;

public interface CloudStorageProvider {

    URL generatePresignedUploadUrl(FileReference fileReference);
    URL generatePresignedDownloadUrl(FileReference fileReference);
    boolean fileExists(String filePath);
    void moveFile(String fromFilePath, String toFilePath);
    void removeFile(String filePath);
    void removeFiles(String directory, List<String> fileNames);
    void copyFile(String fromFilePath, String toFilePath);

}
