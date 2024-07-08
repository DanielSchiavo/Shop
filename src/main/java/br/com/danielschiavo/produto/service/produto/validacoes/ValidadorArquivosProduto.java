package br.com.danielschiavo.produto.service.produto.validacoes;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
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
		List<String> nomes = request.arquivos().stream().map(ArquivoProdutoDTO::nome).collect(Collectors.toList());

		List<ArquivoInfoDTO> arquivoInfoDTOS = fileStorageProdutoService.pegarImagens(nomes);
		arquivoInfoDTOS.forEach(arquivo -> {
			if (arquivo.erro() != null) {
				throw new ValidacaoException("Não foi possivel cadastrar o produto porque a imagem " + arquivo.nomeArquivo() + " não existe");
			}
		});
	}

}
