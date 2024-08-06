package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.model.entity.Product;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends ProductFileMapper, ProductDeliveryTypeMapper {

	Product toEntity(DetailProductResponse response);

	@BeanMapping(builder = @Builder(disableBuilder = true))
	@Mapping(target = "deliveryTypes", ignore = true)
	Product toEntity(RegisterProductRequest request);

	@AfterMapping
	default void toEntity(@MappingTarget Product produto, RegisterProductRequest request) {
		mapFilesToEntity(produto, request.files());
		mapDeliveryTypesToEntity(produto, request.deliveryTypes());
	}

	@Mapping(target = "deliveryTypes", ignore = true)
	@Mapping(target = "productFiles", ignore = true)
	@BeanMapping(builder = @Builder(disableBuilder = true),
			nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
			nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateProduct(UpdateProductRequest request, @MappingTarget Product product);

	@AfterMapping
	default void toEntity(@MappingTarget Product product, UpdateProductRequest request) {
		mapFilesToEntity(product, request.files());
		mapDeliveryTypesToEntity(product, request.deliveryTypes());
	}

	@Mapping(target = "firstImage.fileName", expression = "java(product.getNameFirstImage())")
	ShowProductsResponse toShowProducts(Product product);

	List<ShowProductsResponse> toShowProducts(List<Product> products);

	@Mapping(target = "files", source = "productFiles")
	DetailProductResponse toDetailProduct(Product product);
}
