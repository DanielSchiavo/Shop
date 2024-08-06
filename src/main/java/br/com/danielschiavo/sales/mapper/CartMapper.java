package br.com.danielschiavo.sales.mapper;

import br.com.danielschiavo.sales.dto.request.AddCartItemRequest;
import br.com.danielschiavo.sales.dto.response.ShowCartItemResponse;
import br.com.danielschiavo.sales.model.entity.Cart;
import br.com.danielschiavo.sales.dto.response.ShowCartResponse;
import br.com.danielschiavo.sales.model.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "cartItems", source = "cartItems")
	ShowCartResponse toDtoShowCart(Cart cart);

	List<ShowCartItemResponse> toDtoShowCartItem(List<CartItem> items);

	CartItem toEntity(AddCartItemRequest request);
	
}
