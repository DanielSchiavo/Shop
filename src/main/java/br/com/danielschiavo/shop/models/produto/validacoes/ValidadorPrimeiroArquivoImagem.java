package br.com.danielschiavo.shop.models.produto.validacoes;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProdutoDTO;

@Service
public class ValidadorPrimeiroArquivoImagem implements ValidadorCadastrarNovoProduto {

	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		Optional<ArquivoProdutoDTO> first = cadastrarProdutoDTO.arquivos().stream().filter(arq -> arq.posicao() == 0).findFirst();
		if (first.isPresent()) {
			String nomeArquivo = first.get().nome();
			if(!nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpg")) {
				throw new ValidacaoException("O arquivo na posição 0 do produto sempre deve ser uma imagem");
			}
		}
	}

}
