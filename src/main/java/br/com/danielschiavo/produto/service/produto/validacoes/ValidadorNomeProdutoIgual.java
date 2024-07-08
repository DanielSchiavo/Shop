package br.com.danielschiavo.produto.service.produto.validacoes;


import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.entity.Produto;
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
