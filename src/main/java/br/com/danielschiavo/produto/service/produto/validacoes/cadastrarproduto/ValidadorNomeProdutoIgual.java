package br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto;


import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.repository.ProdutoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ValidadorNomeProdutoIgual implements ValidadorCadastrarNovoProduto {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void validar(Produto cadastrarProduto) {
		Optional<Produto> optionalProduto = produtoRepository.findByNomeLowerCase(cadastrarProduto.getNome());
		if (optionalProduto.isPresent()) {
			throw new ValidacaoException("Já existe um produto com esse name!");
		}
	}

}
