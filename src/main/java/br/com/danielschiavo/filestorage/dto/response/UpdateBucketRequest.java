package br.com.danielschiavo.filestorage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Permission;

public record UpdateBucketRequest(
        String name,
        Permission permission
) {
}
