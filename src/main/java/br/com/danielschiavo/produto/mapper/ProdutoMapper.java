package br.com.danielschiavo.produto.mapper;

import br.com.danielschiavo.produto.dto.request.AlterarProdutoRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.response.DetalharProdutoResponse;
import br.com.danielschiavo.produto.dto.response.MostrarProdutosResponse;
import br.com.danielschiavo.produto.model.entity.Produto;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper extends ArquivoProdutoMapper, TipoEntregaProdutoMapper {

	@BeanMapping(builder = @Builder(disableBuilder = true))
	@Mapping(target = "tiposEntrega", ignore = true)
	Produto toEntity(CadastrarProdutoRequest request);

	@AfterMapping
	default void toEntity(@MappingTarget Produto produto, CadastrarProdutoRequest request) {
		mapearArquivoProdutoDtoParaArquivoProduto(produto, request.arquivos());
		mapearTipoEntregaDtoParaTipoEntregaProduto(produto, request.tiposEntrega());
	}

	@Mapping(target = "tiposEntrega", ignore = true)
	@Mapping(target = "arquivosProduto", ignore = true)
	@BeanMapping(builder = @Builder(disableBuilder = true), nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void alterarProdutoDtoParaProduto(Produto produtoAtualizado, @MappingTarget Produto produto);

	@AfterMapping
	default void toEntity(@MappingTarget Produto produto, AlterarProdutoRequest request) {
		mapearArquivoProdutoDtoParaArquivoProduto(produto, request.arquivos());
		mapearTipoEntregaDtoParaTipoEntregaProduto(produto, request.tiposEntrega());
	}

	MostrarProdutosResponse toMostrarProdutosDto(Produto produto);

	DetalharProdutoResponse toDetalharProdutoDto(Produto produto);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Produto toEntity(AlterarProdutoRequest request);
}
