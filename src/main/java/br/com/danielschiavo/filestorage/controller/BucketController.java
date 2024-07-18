package br.com.danielschiavo.filestorage.controller;

import br.com.danielschiavo.filestorage.dto.request.CreateBucketRequest;
import br.com.danielschiavo.filestorage.dto.response.UpdateBucketRequest;
import br.com.danielschiavo.filestorage.mapper.BucketMapper;
import br.com.danielschiavo.filestorage.model.Bucket;
import br.com.danielschiavo.filestorage.service.BucketService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buckets")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "FileStorage - Bucket Service", description = "To manage all buckets")
public class BucketController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BucketService service;

    @Autowired
    private BucketMapper mapper;

    @PostMapping
    public ResponseEntity<?> createBucket(@RequestBody CreateBucketRequest request) {
        Long customerId = securityService.getCustomerId();

        Bucket bucket = service.createBucket(customerId, request);

        return ResponseEntity.ok(Response.success("Bucket created succesfully!", mapper.toDto(bucket)));
    }

    @PostMapping("/{bucketId}")
    public ResponseEntity<?> deleteBucket(@PathVariable Long bucketId) {
        service.deleteBucket(bucketId);

        return ResponseEntity.ok(Response.success("Bucket deleted succesfully!", null));
    }

    @PutMapping("/{bucketId}")
    public ResponseEntity<?> updateBucket(@PathVariable Long bucketId, @RequestBody UpdateBucketRequest request) {
        service.updateBucket(bucketId, request);

        return ResponseEntity.ok(Response.success("Bucket updated succesfully!", null));
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<?> getBucketById(@PathVariable Long bucketId) {
        Bucket bucket = service.getBucketById(bucketId);

        return ResponseEntity.ok(Response.success("Success recovering bucket", bucket));
    }
}
