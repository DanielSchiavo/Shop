package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.UpdateProductRequest;
import br.com.danielschiavo.catalog.dto.request.RegisterProductRequest;
import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.model.entity.Product;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends ProductFileMapper, ProductDeliveryTypeMapper {

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
	@BeanMapping(builder = @Builder(disableBuilder = true), nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void updateProduct(Product updatedProduct, @MappingTarget Product product);

	@AfterMapping
	default void toEntity(@MappingTarget Product product, UpdateProductRequest request) {
		mapFilesToEntity(product, request.files());
		mapDeliveryTypesToEntity(product, request.deliveryTypes());
	}

	ShowProductsResponse toShowProducts(Product produto);

	DetailProductResponse toDetailProduct(Product produto);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Product toEntity(UpdateProductRequest request);
}
