package br.com.danielschiavo.shop.models.produto.validacoes;

import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;

public interface ValidadorCadastrarNovoProduto {
	
	void validar(CadastrarProdutoDTO cadastrarProdutoDTO);

}
