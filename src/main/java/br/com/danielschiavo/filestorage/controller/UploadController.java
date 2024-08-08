package br.com.danielschiavo.filestorage.controller;

import br.com.danielschiavo.filestorage.dto.request.UploadObjectRequest;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.Response;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class UploadController {

    private final FileReferenceService service;

    @PostMapping("/filestorage/uploads")
    public ResponseEntity<?> newDocumentUploadRequest(@RequestBody @Valid UploadObjectRequest request) {
        return ResponseEntity.ok(Response.success("Success generating an upload url", service.generateUploadUrl(request)));
    }

}