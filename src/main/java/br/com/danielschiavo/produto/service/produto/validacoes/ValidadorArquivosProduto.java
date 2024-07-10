package br.com.danielschiavo.produto.service.produto.validacoes;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.filestorage.dto.response.FileInfoResponse;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.dto.ArquivoProdutoDTO;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidadorArquivosProduto implements ValidadorCadastrarNovoProduto {

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;
	
	@Override
	public void validar(CadastrarProdutoRequest request) {
		List<String> nomes = request.arquivos().stream().map(ArquivoProdutoDTO::nome).toList();

		nomes.forEach(nome -> {
			boolean existe = fileStorageProdutoService.verificarSeImagemExiste(nome);
			if (!existe)
				throw new ValidacaoException("Não foi possivel cadastrar o produto porque a imagem " + nome + " não existe");
		});
	}

}
