package br.com.danielschiavo.produto.service.produto.validacoes;

import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;

public interface ValidadorCadastrarNovoProduto {
	
	void validar(CadastrarProdutoRequest request);

}
