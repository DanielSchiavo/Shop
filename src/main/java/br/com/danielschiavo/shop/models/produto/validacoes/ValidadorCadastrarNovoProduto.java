package br.com.danielschiavo.shop.models.produto.validacoes;

import br.com.danielschiavo.shop.models.produto.dto.CadastrarProdutoDTO;

public interface ValidadorCadastrarNovoProduto {
	
	void validar(CadastrarProdutoDTO cadastrarProdutoDTO);

}
