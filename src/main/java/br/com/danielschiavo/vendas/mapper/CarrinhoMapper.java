package br.com.danielschiavo.vendas.mapper;

import br.com.danielschiavo.vendas.model.entity.Carrinho;
import br.com.danielschiavo.vendas.dto.response.MostrarCarrinhoClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class CarrinhoMapper {

	@Mapping(target = "itemsCarrinho", source = "itemsCarrinho")
	@Mapping(target = "valorTotal", ignore = true) 
	public abstract MostrarCarrinhoClienteResponse toDto(Carrinho carrinho);
	
}
