package br.com.danielschiavo.produto.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CadastrarProdutoRequest(
		@NotBlank
		String nome,
		@NotBlank
		String descricao,
		@NotNull
		@Positive
		BigDecimal preco,
		@NotNull
		Integer quantidade,
		@NotNull
		Boolean ativo,
		@NotNull
		Long subCategoriaId,
		@NotNull
		Set<TipoEntrega> tiposEntrega,
		@NotNull
		Set<AdicionarArquivoProdutoRequest> arquivos
		) {	


}
