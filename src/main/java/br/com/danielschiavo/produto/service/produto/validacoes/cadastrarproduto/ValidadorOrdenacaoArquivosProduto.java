package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import java.util.List;
import java.util.stream.IntStream;

import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


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
			throw new ValidationException("As posições dos arquivos do produto não estão seguindo uma ordenação correta");
		}
	}

}
