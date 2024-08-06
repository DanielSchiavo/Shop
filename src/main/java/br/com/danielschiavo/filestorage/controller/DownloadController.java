package br.com.danielschiavo.filestorage.controller;

import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DownloadController {

    @Autowired
    private FileReferenceService service;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/filestorage/downloads/{filePath}")
    public ResponseEntity<Void> downloadRequest(@PathVariable String filePath) {
        var file = service.generateDownloadUrl(filePath, securityService.isAdmin());

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", file.url()).build();
    }

}
