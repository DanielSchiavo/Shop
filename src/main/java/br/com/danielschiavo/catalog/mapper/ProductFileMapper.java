package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.product.AddProductFileRequest;
import br.com.danielschiavo.catalog.dto.response.product.DetailProductFileResponse;
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
}
