package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;

public interface ValidadorCadastrarNovoProduto {
	
	void validar(Produto cadastrarProduto);

}
