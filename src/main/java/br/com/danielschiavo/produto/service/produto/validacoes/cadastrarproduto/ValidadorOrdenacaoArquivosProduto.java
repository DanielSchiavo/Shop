package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import java.util.List;
import java.util.stream.IntStream;

import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;


@Service
public class ValidadorOrdenacaoArquivosProduto implements ValidadorCadastrarNovoProduto {

	@Override
	public void validar(Produto cadastrarProduto) {
        List<Byte> posicoesOrdenadas = cadastrarProduto.getArquivosProduto().stream()
									                .map(ArquivoProduto::getPosicao)
									                .sorted()
									                .toList();

		boolean allMatch = IntStream.range(0, posicoesOrdenadas.size())
									.allMatch(i -> i == posicoesOrdenadas.get(i));
		
		if (!allMatch) {
			throw new ValidacaoException("As posições dos arquivos do produto não estão seguindo uma ordenação correta");
		}
	}

}
