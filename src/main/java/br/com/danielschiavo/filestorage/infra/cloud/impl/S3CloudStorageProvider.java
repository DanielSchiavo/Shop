package br.com.danielschiavo.filestorage.infra.cloud.impl;

import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.infra.cloud.CloudStorageProvider;
import br.com.danielschiavo.filestorage.model.FileReference;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class S3CloudStorageProvider implements CloudStorageProvider {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final StorageProperties storageProperties;

    @Override
    public URL generatePresignedUploadUrl(FileReference fileReference) {
        try (S3Presigner presigner = this.s3Presigner) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(getBucket())
                    .key(fileReference.getFile().getFileName() + "/" + fileReference.getFile().getDirectory())
                    .contentType(fileReference.getContentType())
                    .contentLength(fileReference.getContentLength())
                    .build();

            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(300))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(putObjectPresignRequest);

            return presignedPutObjectRequest.url();
        }
    }

    @Override
    public URL generatePresignedDownloadUrl(FileReference fileReference) {
        try (S3Presigner presigner = this.s3Presigner) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(getBucket())
                    .key(fileReference.getFile().getFileName() + "/" + fileReference.getFile().getDirectory())
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            return presignedRequest.url();
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(builder -> builder.bucket(getBucket()).key(filePath));
            return true;
        } catch (NoSuchKeyException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException(e.getMessage());
        }
    }

    @Override
    public void moveFile(String fromFilePath, String toFilePath) {

    }

    @Override
    public void removeFile(String filePath) {
        s3Client.deleteObject(builder -> builder.bucket(getBucket()).key(filePath));
    }

    @Override
    public void removeFiles(String directory, List<String> fileNames) {
        try {
            // Cria a lista de objetos a serem excluídos
            List<ObjectIdentifier> objectIdentifiers = fileNames.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());

            // Cria a solicitação de exclusão
            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(getBucket())
                    .delete(deleteBuilder -> deleteBuilder.objects(objectIdentifiers))
                    .build();

            // Exclui os objetos
            DeleteObjectsResponse response = s3Client.deleteObjects(deleteObjectsRequest);

            // Processa a resposta (mostra os erros se houver)
            response.deleted().forEach(deleted ->
                    System.out.println("Objeto excluído: " + deleted.key())
            );

            response.errors().forEach(error ->
                    System.err.println("Erro ao excluir objeto: " + error.key() + " - " + error.message())
            );

        } catch (S3Exception e) {
            System.err.println("Erro ao excluir objetos: " + e.getMessage());
        }
    }

    @Override
    public void copyFile(String fromFilePath, String toFilePath) {
        s3Client.copyObject(builder -> builder
                .sourceBucket(getBucket())
                .sourceKey(fromFilePath)
                .destinationBucket(getBucket())
                .destinationKey(toFilePath).build());
    }

    private String getBucket() {
        return storageProperties.getS3().getBucket();
    }
}
