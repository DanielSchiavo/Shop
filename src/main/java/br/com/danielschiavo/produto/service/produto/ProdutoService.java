package br.com.danielschiavo.produto.service.produto;

import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.mapper.ProdutoMapper;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.repository.ProdutoRepository;
import br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto.ValidadorCadastrarNovoProduto;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;

	@Autowired
	private ProdutoMapper mapper;

	@Autowired
	private List<ValidadorCadastrarNovoProduto> validador;
	
	@Transactional
	public void deletarProdutoPorId(Long id) {
		Produto produto = pegarProdutoPorId(id);
		List<String> nomeTodosArquivos = produto.pegarNomeTodosArquivos();
		
		fileStorageProdutoService.deletarImagens(nomeTodosArquivos);
		produtoRepository.delete(produto);
	}
	
	@Transactional
	public Produto cadastrarProduto(Produto cadastrarProduto) {
		validador.forEach(v -> v.validar(cadastrarProduto));
		
		return produtoRepository.save(cadastrarProduto);
	}

	@Transactional
	public Produto alterarProdutoPorId(Long id, Produto produtoAtualizado) {
		Produto produto = pegarProdutoPorId(id);
		mapper.alterarProdutoDtoParaProduto(produtoAtualizado, produto);
		return produtoRepository.save(produto);
	}

	public Page<Produto> listarProdutos(Pageable pageable) {
		return produtoRepository.findAll(pageable);
	}

	public Produto pegarProdutoPorId(Long id) {
		return produtoRepository.findById(id)
				.orElseThrow(() -> new ValidacaoException("Não existe um produto com o id " + id));
	}

	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


}
