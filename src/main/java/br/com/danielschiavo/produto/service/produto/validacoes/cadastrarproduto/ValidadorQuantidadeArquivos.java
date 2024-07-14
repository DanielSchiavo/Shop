package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorQuantidadeArquivos implements ValidadorCadastrarNovoProduto {

	private final int MAX_FILES = 10;
	
	@Override
	public void validar(Produto cadastrarProduto) {
		if (cadastrarProduto.getArquivosProduto().size() > MAX_FILES) {
			throw new ValidationException("O máximo de arquivos para produto são " + MAX_FILES);
		}
	}


}
