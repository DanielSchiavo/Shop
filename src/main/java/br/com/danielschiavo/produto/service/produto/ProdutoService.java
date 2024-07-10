package br.com.danielschiavo.produto.service.produto;

import java.util.List;

import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.dto.request.AlterarProdutoRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.mapper.ProdutoMapper;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.repository.ProdutoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.produto.service.produto.validacoes.cadastrarproduto.ValidadorCadastrarNovoProduto;
import lombok.Setter;

@Service
public class ProdutoService {
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;

	@Autowired
	private List<ValidadorCadastrarNovoProduto> validador;
	
	@Setter
	@Autowired
	private ProdutoMapper produtoMapper;
	
	@Transactional
	public void deletarProdutoPorId(Long id) {
		Produto produto = pegarProdutoPorId(id);
		List<String> nomeTodosArquivos = produto.pegarNomeTodosArquivos();
		
		fileStorageProdutoService.deletarImagens(nomeTodosArquivos);
		produtoRepository.delete(produto);
	}
	
	@Transactional
	public Produto cadastrarProduto(CadastrarProdutoRequest request) {
		validador.forEach(v -> v.validar(request));
		
		Produto produto = produtoMapper.toEntity(request);
		produtoRepository.save(produto);
		
		return produto;
	}

	@Transactional
	public Produto alterarProdutoPorId(Long id, AlterarProdutoRequest request) {
		Produto produto = pegarProdutoPorId(id);
		produtoMapper.alterarProdutoDtoParaProduto(request, produto);
		produtoRepository.save(produto);

		return produto;
	}

	public Page<Produto> listarProdutos(Pageable pageable) {
		return produtoRepository.findAll(pageable);
	}

	public Produto pegarProdutoPorId(Long id) {
		return produtoRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe um produto com o id " + id));
	}

	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


}
