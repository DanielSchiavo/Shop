package br.com.danielschiavo.shop.models.produto.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProdutoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CadastrarProdutoDTO(
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
		Long idSubCategoria,
		@NotNull
		Set<TipoEntrega> tipoEntrega,
		@NotNull
		List<ArquivoProdutoDTO> arquivos
		) {

}
