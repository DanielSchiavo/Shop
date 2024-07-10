package br.com.danielschiavo.produto.mapper;

import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.enums.TipoEntregaProduto;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TipoEntregaProdutoMapper {

	default void mapearTipoEntregaDtoParaTipoEntregaProduto(Produto produto, Set<TipoEntrega> request) {
		Set<TipoEntregaProduto> tiposEntrega = request.stream()
				.map(tipo -> new TipoEntregaProduto(null, tipo, produto))
				.collect(Collectors.toSet());
		produto.adicionarTiposEntrega(tiposEntrega);
	}
}
