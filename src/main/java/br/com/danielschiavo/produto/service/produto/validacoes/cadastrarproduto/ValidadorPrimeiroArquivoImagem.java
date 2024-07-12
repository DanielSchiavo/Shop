package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import java.util.Optional;

import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorPrimeiroArquivoImagem implements ValidadorCadastrarNovoProduto {

	@Override
	public void validar(Produto cadastrarProduto) {
		Optional<ArquivoProduto> first = cadastrarProduto.getArquivosProduto().stream().filter(arq -> arq.getPosicao() == 0).findFirst();
		if (first.isPresent()) {
			String nomeArquivo = first.get().getNome();
			if(!nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpg")) {
				throw new ValidacaoException("O arquivo na posição 0 do produto sempre deve ser uma imagem");
			}
		}
	}

}
