package br.com.danielschiavo.filestorage.controller;

import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileService;
import br.com.danielschiavo.shared.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/buckets")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "File Storage Service", description = "To manage all files")
public class FileController {

    @Autowired
    private FileService service;

    private final String respMessage = "Request made successfully";

    @GetMapping("/{bucketName}/files/{fileName}")
    @Operation(summary = "Get a file from a bucket")
    public ResponseEntity<?> getFile(@PathVariable String bucketName, @PathVariable String fileName) {
        File file = service.getFile(bucketName, fileName);

        FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Success recovering file", file.getContent());
        return ResponseEntity.ok(Response.success(respMessage, fileInfoResponse));
    }

    @PostMapping("/{bucketName}/files/{fileName}")
    @Operation(summary = "Register a file into a bucket")
    public ResponseEntity<?> registerFile(@PathVariable String bucketName, @PathVariable String fileName, @RequestPart MultipartFile multipartFile) throws IOException {
        File file = service.registerFile(bucketName, fileName, multipartFile.getBytes(), multipartFile.getContentType());

        FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "Success registering file", null);
        return ResponseEntity.ok(Response.success(respMessage, fileInfoResponse));
    }

    @DeleteMapping("/{bucketId}/files/{fileName}")
    @Operation(summary = "Delete a file into a bucket")
    public ResponseEntity<?> deleteFile(@PathVariable String bucketId, @PathVariable String fileName) {
        service.deleteFile(bucketId, fileName);

        FileInfoResponse fileInfoResponse = FileInfoResponse.success(fileName, "File deleted successfully!", null);
        return ResponseEntity.ok(Response.success(respMessage, fileInfoResponse));
    }

    @PutMapping("/{destBucketName}/files/{destFileName}")
    @Operation(summary = "Copy a file from a bucket to another")
    public ResponseEntity<?> copyFileWithGivenDestFileName(
                                      @PathVariable String destBucketName,
                                      @PathVariable String destFileName,
                                      @RequestHeader("fs-copy-source") String copySource) {
        String[] split = copySource.split("/");
        String sourceBucketName = split[0];
        String sourceFileName = split[1];

        File file = service.copyFile(destBucketName, destFileName, sourceBucketName, sourceFileName);

        FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "File copied successfully!", null);
        return ResponseEntity.ok(Response.success(respMessage, fileInfoResponse));
    }

    @PutMapping("/{destBucketName}/files")
    @Operation(summary = "Copy a file from a bucket to another")
    public ResponseEntity<?> copyFileWithoutGivenDestFileName(
                                @PathVariable String destBucketName,
                                @RequestHeader("fs-copy-source") String copySource) {
        String[] split = copySource.split("/");
        String sourceBucketName = split[0];
        String sourceFileName = split[1];

        File file = service.copyFile(destBucketName, null, sourceBucketName, sourceFileName);

        FileInfoResponse fileInfoResponse = FileInfoResponse.success(file.getFileName(), "File copied successfully!", null);
        return ResponseEntity.ok(Response.success(respMessage, fileInfoResponse));
    }

    @RequestMapping(value = "/{bucketName}/files/{fileName}", method = RequestMethod.HEAD)
    @Operation(summary = "Check if the file exists")
    public ResponseEntity<?> checkIfFileExists(@PathVariable String bucketName, @PathVariable String fileName) {
        File file = service.getFile(bucketName, fileName);

        return ResponseEntity.ok()
                .header("Date", String.valueOf(LocalDateTime.now()))
                .contentType(MediaType.valueOf(file.getContentType()))
                .lastModified(ZonedDateTime.from(file.getLastModifiedDateTime()))
                .contentLength(file.getContent().length)
                .build();
    }
}
