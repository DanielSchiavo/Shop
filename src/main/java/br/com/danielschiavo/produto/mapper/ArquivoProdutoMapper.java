package br.com.danielschiavo.produto.mapper;

import java.util.Set;

import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import br.com.danielschiavo.produto.dto.ArquivoProdutoDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArquivoProdutoMapper {

	@AfterMapping
	default void arquivoProdutoDTOParaArquivoProduto(@MappingTarget Produto produto, Set<ArquivoProdutoDTO> arquivos) {
		arquivos.forEach(arquivo -> {
			ArquivoProduto arquivoProduto = new ArquivoProduto();
			arquivoProduto.setNome(arquivo.nome());
			arquivoProduto.setPosicao(arquivo.posicao());
			arquivoProduto.setProduto(produto);
			produto.adicionarArquivoProduto(arquivoProduto);
		});

	}
	
}
