package br.com.danielschiavo.produto.service.admin.validacoes;

import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorQuantidadeArquivos implements ValidadorCadastrarNovoProduto {

	private final int MAX_FILES = 10;
	
	@Override
	public void validar(CadastrarProdutoRequest request) {
		if (request.arquivos().size() > MAX_FILES) {
			throw new ValidacaoException("O máximo de arquivos para produto são " + MAX_FILES);
		}
	}


}
