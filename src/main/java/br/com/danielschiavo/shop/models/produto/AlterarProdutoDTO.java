package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProdutoDTO;
import jakarta.validation.constraints.NotNull;

public record AlterarProdutoDTO(
		String nome,
		String descricao,
		BigDecimal preco,
		Integer quantidade,
		Boolean ativo,
		Long idCategoria,
		Long idSubCategoria,
		Set<TipoEntrega> tipoEntrega,
		@NotNull
		List<ArquivoProdutoDTO> arquivos
		) {

}
