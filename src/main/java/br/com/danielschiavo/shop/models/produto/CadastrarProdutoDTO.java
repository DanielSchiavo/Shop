package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProdutoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
		Long idCategoria,
		@NotNull
		Long idSubCategoria,
		@NotNull
		Set<TipoEntrega> tipoEntrega,
		@NotNull
		List<ArquivoProdutoDTO> arquivos
		) {

}
