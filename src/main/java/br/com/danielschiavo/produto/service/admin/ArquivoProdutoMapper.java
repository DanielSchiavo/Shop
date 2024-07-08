package br.com.danielschiavo.produto.service.admin;

import java.util.HashSet;
import java.util.Set;

import br.com.danielschiavo.produto.model.AlterarProdutoRequest;
import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.Produto;
import br.com.danielschiavo.produto.model.arquivosproduto.ArquivoProduto;
import br.com.danielschiavo.produto.model.arquivosproduto.ArquivoProdutoDTO;
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
