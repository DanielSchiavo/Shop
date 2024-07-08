package br.com.danielschiavo.produto.service.admin;

import java.util.HashSet;
import java.util.Set;

import br.com.danielschiavo.pedido.model.TipoEntrega;
import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.Produto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import br.com.danielschiavo.produto.model.tipoentregaproduto.TipoEntregaProduto;

@Mapper(componentModel = "spring")
public interface TipoEntregaProdutoMapper {
	
    @AfterMapping
	default void setTiposEntregaParaSetTiposEntregaProduto(@MappingTarget Produto produto, Set<TipoEntrega> tiposEntrega) {
		tiposEntrega.forEach(tipoEntrega -> {
			TipoEntregaProduto tipoEntregaProduto = new TipoEntregaProduto();
			tipoEntregaProduto.setTipoEntrega(tipoEntrega);
			tipoEntregaProduto.setProduto(produto);
			produto.adicionarTipoEntrega(tipoEntregaProduto);
		});
	}
	
}
