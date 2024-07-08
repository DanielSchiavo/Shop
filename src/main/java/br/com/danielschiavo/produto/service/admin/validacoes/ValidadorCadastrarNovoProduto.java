package br.com.danielschiavo.produto.service.admin.validacoes;

import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;

public interface ValidadorCadastrarNovoProduto {
	
	void validar(CadastrarProdutoRequest request);

}
