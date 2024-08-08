package br.com.danielschiavo.shared;

import br.com.danielschiavo.catalog.dto.response.product.DetailProductFileResponse;
import br.com.danielschiavo.catalog.service.category.CategoryService;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FileMapper {

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    private FileReferenceService fileService;

    public void mapFilesToDto(List<DetailFileResponse> filesResponse,
                              String directory,
                              Set<String> fileNames) {

        String downloadUrl = storageProperties.getImage().getDownloadUrl().toString();
        List<DetailFileReferenceResponse> filesReferences = fileService.getAllById(directory, fileNames);

        filesResponse.forEach(fileResponse -> {
            DetailFileReferenceResponse reference = filesReferences.stream().filter(f -> f.fileName().equals(fileResponse.getFileName())).findFirst().get();

            fileResponse.setDirectory(reference.directory());
            fileResponse.setContentType(reference.contentType());
            fileResponse.setDownloadUrl(downloadUrl + "/" + fileResponse.getFileName());
            fileResponse.setContentLength(reference.contentLength());
        });

    }
}
