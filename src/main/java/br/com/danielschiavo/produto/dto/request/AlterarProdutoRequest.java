package br.com.danielschiavo.produto.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import lombok.Builder;

@Builder
public record AlterarProdutoRequest(
		String nome,
		String descricao,
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		Long subCategoriaId,
		Set<AdicionarArquivoProdutoRequest> arquivos,
		Set<TipoEntrega> tiposEntrega
		) {

}
