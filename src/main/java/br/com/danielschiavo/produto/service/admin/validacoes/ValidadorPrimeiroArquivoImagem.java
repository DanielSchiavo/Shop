package br.com.danielschiavo.produto.service.admin.validacoes;

import java.util.Optional;

import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.arquivosproduto.ArquivoProdutoDTO;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorPrimeiroArquivoImagem implements ValidadorCadastrarNovoProduto {

	@Override
	public void validar(CadastrarProdutoRequest request) {
		Optional<ArquivoProdutoDTO> first = request.arquivos().stream().filter(arq -> arq.posicao() == 0).findFirst();
		if (first.isPresent()) {
			String nomeArquivo = first.get().nome();
			if(!nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpg")) {
				throw new ValidacaoException("O arquivo na posição 0 do produto sempre deve ser uma imagem");
			}
		}
	}

}
