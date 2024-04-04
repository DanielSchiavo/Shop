package br.com.danielschiavo.shop.models.produto.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.produto.dto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.services.filestorage.FileStorageProdutoService;

@Service
public class ValidadorArquivosProduto implements ValidadorCadastrarNovoProduto {

	@Autowired
	private FileStorageProdutoService fileService;
	
	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		cadastrarProdutoDTO.arquivos().forEach(arquivo -> {
			fileService.verificarSeExisteArquivoProdutoPorNome(arquivo.nome());
		});
	}

}
