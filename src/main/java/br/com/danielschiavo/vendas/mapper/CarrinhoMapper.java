package br.com.danielschiavo.vendas.mapper;

import br.com.danielschiavo.vendas.dto.request.ItemCarrinhoRequest;
import br.com.danielschiavo.vendas.model.entity.Carrinho;
import br.com.danielschiavo.vendas.dto.response.MostrarCarrinhoClienteResponse;
import br.com.danielschiavo.vendas.model.entity.ItemCarrinho;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

	@Mapping(target = "itemsCarrinho", source = "itemsCarrinho")
	MostrarCarrinhoClienteResponse toDto(Carrinho carrinho);

	ItemCarrinho toEntity(ItemCarrinhoRequest request);
	
}
