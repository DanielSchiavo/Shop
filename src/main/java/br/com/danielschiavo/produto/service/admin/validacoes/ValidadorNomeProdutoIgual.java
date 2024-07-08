package br.com.danielschiavo.produto.service.admin.validacoes;


import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.Produto;
import br.com.danielschiavo.produto.repository.admin.ProdutoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ValidadorNomeProdutoIgual implements ValidadorCadastrarNovoProduto {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void validar(CadastrarProdutoRequest cadastrarProdutoDTO) {
		Optional<Produto> optionalProduto = produtoRepository.findByNomeLowerCase(cadastrarProdutoDTO.nome());
		if (optionalProduto.isPresent()) {
			throw new ValidacaoException("Já existe um produto com esse nome!");
		}
	}

}
