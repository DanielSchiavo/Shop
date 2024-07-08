package br.com.danielschiavo.produto.mapper;

import br.com.danielschiavo.produto.dto.request.AlterarProdutoRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.response.DetalharProdutoResponse;
import br.com.danielschiavo.produto.dto.response.MostrarProdutosResponse;
import br.com.danielschiavo.produto.model.entity.Produto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper extends TipoEntregaProdutoMapper, ArquivoProdutoMapper {

	@Mapping(target = "tiposEntrega", ignore = true)
	@Mapping(target = "arquivosProduto", ignore = true)
	@BeanMapping(builder = @Builder(disableBuilder = true))
	Produto cadastrarProdutoDtoParaProduto(CadastrarProdutoRequest request);
	
	@Mapping(target = "tiposEntrega", ignore = true)
	@Mapping(target = "arquivosProduto", ignore = true)
	@BeanMapping(builder = @Builder(disableBuilder = true), nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void alterarProdutoDtoParaProduto(AlterarProdutoRequest request, @MappingTarget Produto produto);

	MostrarProdutosResponse toMostrarProdutosDto(Produto produto);

	DetalharProdutoResponse toDetalharProdutoDto(Produto produto);
}
