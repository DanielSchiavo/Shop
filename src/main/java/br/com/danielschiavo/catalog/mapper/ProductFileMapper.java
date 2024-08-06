package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.AddProductFileRequest;
import br.com.danielschiavo.catalog.dto.response.DetailProductFileResponse;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductFileMapper {

    default void mapFilesToEntity(Product product, Set<AddProductFileRequest> request) {
        Set<ProductFile> productFiles = request.stream()
                .map(file -> new ProductFile(null, file.fileReferenceId(), file.type(), file.urlVideo(), file.position(), product))
                .collect(Collectors.toSet());
        product.addProductFiles(productFiles);
    }

    default void mapFilesToDto(List<DetailProductFileResponse> productFiles,
                               List<DetailFileReferenceResponse> filesReferences,
                               String downloadUrl) {
        productFiles.forEach(pf -> {
            DetailFileReferenceResponse fr = filesReferences.stream().filter(f -> f.fileName().equals(pf.getFileName())).findFirst().get();

            pf.setDirectory(fr.directory());
            pf.setContentType(fr.contentType());
            pf.setDownloadUrl(downloadUrl + "/" + pf.getFileName());
            pf.setContentLength(fr.contentLength());
        });
    }

}
