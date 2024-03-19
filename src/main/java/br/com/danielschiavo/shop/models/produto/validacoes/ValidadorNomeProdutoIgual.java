package br.com.danielschiavo.shop.models.produto.validacoes;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@Service
public class ValidadorNomeProdutoIgual implements ValidadorCadastrarNovoProduto {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		Optional<Produto> optionalProduto = produtoRepository.findByNomeLowerCase(cadastrarProdutoDTO.nome());
		if (optionalProduto.isPresent()) {
			throw new ValidacaoException("Já existe um produto com esse nome!");
		}
	}

}
