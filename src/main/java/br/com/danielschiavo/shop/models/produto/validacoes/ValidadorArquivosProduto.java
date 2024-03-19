package br.com.danielschiavo.shop.models.produto.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.services.FileStorageService;

@Service
public class ValidadorArquivosProduto implements ValidadorCadastrarNovoProduto {

	@Autowired
	private FileStorageService fileService;
	
	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		cadastrarProdutoDTO.arquivos().forEach(arquivo -> {
			fileService.verificarSeExisteArquivoProdutoPorNome(arquivo.nome());
		});
	}

}
