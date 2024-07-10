package br.com.danielschiavo.produto.mapper;

import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArquivoProdutoMapper {

    default void mapearArquivoProdutoDtoParaArquivoProduto(Produto produto, Set<AdicionarArquivoProdutoRequest> request) {
        Set<ArquivoProduto> arquivos = request.stream()
                .map(arquivo -> new ArquivoProduto(null, arquivo.nome(), arquivo.posicao(), produto))
                .collect(Collectors.toSet());
        produto.adicionarArquivosProduto(arquivos);
    }


	
}
