package br.com.danielschiavo.produto.mapper;

import br.com.danielschiavo.pedido.model.enums.DeliveryType;
import br.com.danielschiavo.produto.model.entity.Product;
import br.com.danielschiavo.produto.model.enums.ProductDeliveryType;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductDeliveryTypeMapper {

	default void mapDeliveryTypesToEntity(Product product, Set<DeliveryType> request) {
		Set<ProductDeliveryType> deliveryTypes = request.stream()
				.map(type -> new ProductDeliveryType(null, type, product))
				.collect(Collectors.toSet());
		product.addDeliveryType(deliveryTypes);
	}
}
