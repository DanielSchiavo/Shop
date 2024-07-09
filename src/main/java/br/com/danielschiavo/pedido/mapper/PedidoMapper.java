package br.com.danielschiavo.pedido.mapper;

import br.com.danielschiavo.pedido.dto.response.pedido.MostrarPedidoResponse;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    MostrarPedidoResponse toDto(Pedido pedido);
}
