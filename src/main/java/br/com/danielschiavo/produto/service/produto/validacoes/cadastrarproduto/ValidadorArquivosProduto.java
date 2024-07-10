package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;

import java.util.List;

import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.AdicionarArquivoProdutoRequest;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidadorArquivosProduto implements ValidadorCadastrarNovoProduto {

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;
	
	@Override
	public void validar(CadastrarProdutoRequest request) {
		List<String> nomes = request.arquivos().stream().map(AdicionarArquivoProdutoRequest::nome).toList();

		nomes.forEach(nome -> {
			boolean existe = fileStorageProdutoService.verificarSeImagemExiste(nome);
			if (!existe)
				throw new ValidacaoException("Não foi possivel cadastrar o produto porque a imagem " + nome + " não existe");
		});
	}

}
