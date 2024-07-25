package br.com.danielschiavo.sales.mapper;

import br.com.danielschiavo.sales.dto.request.AddCartItemRequest;
import br.com.danielschiavo.sales.model.entity.Cart;
import br.com.danielschiavo.sales.dto.response.ShowCartResponse;
import br.com.danielschiavo.sales.model.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "cartItems", source = "cartItems")
	ShowCartResponse toDtoShowCart(Cart cart);

	CartItem toEntity(AddCartItemRequest request);
	
}
