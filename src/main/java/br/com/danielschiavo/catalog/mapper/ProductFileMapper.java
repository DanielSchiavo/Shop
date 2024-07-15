package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.AddProductFileRequest;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductFileMapper {

    default void mapFilesToEntity(Product product, Set<AddProductFileRequest> request) {
        Set<ProductFile> productFiles = request.stream()
                .map(file -> new ProductFile(null, file.name(), file.position(), product))
                .collect(Collectors.toSet());
        product.addProductFiles(productFiles);
    }


	
}
