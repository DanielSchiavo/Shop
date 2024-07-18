package br.com.danielschiavo.filestorage.dto.response;

import java.security.Permission;

public record UpdateBucketRequest(
        String bucketName,
        Permission permission
) {
}
