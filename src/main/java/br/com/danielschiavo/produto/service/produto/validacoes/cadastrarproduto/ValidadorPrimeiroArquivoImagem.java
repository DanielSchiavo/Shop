package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import java.util.Optional;

import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorPrimeiroArquivoImagem implements ValidadorCadastrarNovoProduto {

	@Override
	public void validar(CadastrarProdutoRequest request) {
		Optional<AdicionarArquivoProdutoRequest> first = request.arquivos().stream().filter(arq -> arq.posicao() == 0).findFirst();
		if (first.isPresent()) {
			String nomeArquivo = first.get().nome();
			if(!nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpg")) {
				throw new ValidacaoException("O arquivo na posição 0 do produto sempre deve ser uma imagem");
			}
		}
	}

}
